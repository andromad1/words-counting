package ua.com.andromad.misc.wordsCount;

import ua.com.andromad.misc.wordsCount.analyze.WordOccurrence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * The main window of the program
 */
public class MainWindowController implements Initializable {
    @FXML private Label lFilePath;
    @FXML private Label lStatus;
    @FXML private Button bChooseFile;
    @FXML private Button bStartAnalysing;
    @FXML private Button bInterruptAnalysing;
    @FXML private ProgressBar pbCompleted;
    @FXML private TableView<WordOccurrenceDataModel> tWordsOccurrence;
    @FXML private TableColumn<WordOccurrenceDataModel, String> tcWord;
    @FXML private TableColumn<WordOccurrenceDataModel, Integer> tcOccurrence;

    private Stage appStage;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private AnalyzeTextFileTask analyzeTask;
    private File chosenFile;
    private ObservableList<WordOccurrenceDataModel> resData;

    public void setStage(Stage stage) {
        appStage = stage;

        appStage.setOnCloseRequest(windowEvent -> {
            if (analyzeTask != null) {
                analyzeTask.cancel();
            }
            executor.shutdown();
            System.exit(0);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tcWord.setCellValueFactory(new PropertyValueFactory<>("word"));
        tcOccurrence.setCellValueFactory(new PropertyValueFactory<>("occurrence"));

        bChooseFile.setOnAction(actionEvent -> {
            FileChooser dialogFC = new FileChooser();
            File dir = (chosenFile!=null && chosenFile.getParentFile()!=null ? chosenFile.getParentFile() : new File("."));
            dialogFC.setInitialDirectory(dir);
            dialogFC.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text files", "*.txt"),
                    new FileChooser.ExtensionFilter("CSV files", "*.csv")
            );

            File retChosenFile = dialogFC.showOpenDialog(appStage);

            if (retChosenFile != null) {
                chosenFile = retChosenFile;
                lFilePath.setText(chosenFile.getAbsolutePath());
                bStartAnalysing.setDisable(false);
                bStartAnalysing.requestFocus();
                lStatus.setText("Not started");
                lStatus.setTextFill(Color.BLACK);
                tWordsOccurrence.setItems(null);
                resData = null;
            }
        });

        bStartAnalysing.setOnAction(actionEvent -> {
            bChooseFile.setDisable(true);
            bInterruptAnalysing.setDisable(false);
            bStartAnalysing.setDisable(true);
            pbCompleted.setVisible(true);
            lStatus.setTextFill(Color.BLACK);
            tWordsOccurrence.setItems(null);
            resData = null;

            analyzeTask = new AnalyzeTextFileTask(chosenFile, true);

            analyzeTask.setOnScheduled(workerStateEvent -> {
                lStatus.setText("Task scheduled");
            });

            analyzeTask.setOnRunning(workerStateEvent -> {
                lStatus.setText("Processing...");
                pbCompleted.progressProperty().bind(analyzeTask.progressProperty());
            });

            analyzeTask.setOnFailed(workerStateEvent -> {
                bChooseFile.setDisable(false);
                bInterruptAnalysing.setDisable(true);
                bStartAnalysing.setDisable(false);
                pbCompleted.setVisible(false);
                pbCompleted.progressProperty().unbind();
                lStatus.setText("Analysing failed due to "+analyzeTask.getException());
                analyzeTask.getException().printStackTrace();
                lStatus.setTextFill(Color.RED);
                analyzeTask = null;
            });

            analyzeTask.setOnCancelled(workerStateEvent -> {
                pbCompleted.progressProperty().unbind();
                analyzeTask = null;
            });

            analyzeTask.setOnSucceeded(workerStateEvent -> {
                pbCompleted.progressProperty().unbind();
                bChooseFile.setDisable(false);
                bInterruptAnalysing.setDisable(true);
                bStartAnalysing.setDisable(false);
                pbCompleted.setVisible(false);
                lStatus.setText("Done!");
                lStatus.setTextFill(Color.GREEN);

                Set<WordOccurrence> res = new TreeSet<>();
                try {
                    res = analyzeTask.get();
                } catch (InterruptedException e) {
                    lStatus.setText("Done, but InterruptedException was thrown while getting result data");
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    lStatus.setText("Done, but ExecutionException was thrown while getting result data");
                    e.printStackTrace();
                }
                resData = res.stream().map(wo -> new WordOccurrenceDataModel(wo.getWord(), wo.getOccurrence()))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));

                tWordsOccurrence.setItems(resData);

                analyzeTask = null;
            });

            executor.execute(analyzeTask);
        });

        bInterruptAnalysing.setOnAction(actionEvent -> {
            boolean resOfCancelling;

            if (analyzeTask != null) {
                resOfCancelling = analyzeTask.cancel();
            } else {
                resOfCancelling = true;
            }

            if (resOfCancelling) {
                bChooseFile.setDisable(false);
                bChooseFile.requestFocus();
                bInterruptAnalysing.setDisable(true);
                bStartAnalysing.setDisable(false);
                pbCompleted.setVisible(false);
                lStatus.setText("Analysing interrupted!");
                lStatus.setTextFill(Color.RED);
            } else {
                lStatus.setText("Analyzing was unable to interrupt!");
                lStatus.setTextFill(Color.RED);
            }
        });
    }
}

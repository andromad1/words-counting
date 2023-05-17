package ua.com.andromad.misc.wordsCount;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * The main class of the program (with GUI)
 */
public class GUIwordsCount extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader  = new FXMLLoader(Objects.requireNonNull(getClass().getResource("mainWindow.fxml")));
        Parent root = loader.load();
        MainWindowController controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.setTitle("Words counting");
        controller.setStage(stage);
        stage.show();
    }
}

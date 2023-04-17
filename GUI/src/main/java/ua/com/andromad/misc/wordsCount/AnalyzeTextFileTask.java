package ua.com.andromad.misc.wordsCount;

import ua.com.andromad.misc.wordsCount.analyze.AnalyzingProcess;
import ua.com.andromad.misc.wordsCount.analyze.ProcessAnalyzing;
import ua.com.andromad.misc.wordsCount.analyze.WordOccurrence;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.concurrent.Task;

/**
 * The class is designed to run the process of parsing a file in an asynchronous mode so as not to block the user
 * interface of the program.
 */
public class AnalyzeTextFileTask extends Task<Set<WordOccurrence>> {
    private final File fileToAnalyze;
    private final boolean isAsync;

    public AnalyzeTextFileTask(File file, boolean isAsync) {
        fileToAnalyze = file;
        this.isAsync = isAsync;
    }

    @Override
    protected Set<WordOccurrence> call() throws InterruptedException, ExecutionException, IOException {
        Set<WordOccurrence> words = new TreeSet<>();

        if (!isAsync) {
            words = ProcessAnalyzing.startAnalyze(fileToAnalyze);
        } else {
            AnalyzingProcess ap = ProcessAnalyzing.startAnalyzeThread(fileToAnalyze);
            FutureTask<Set<WordOccurrence>> ft = ap.getAnalyzingTask();
            Thread t = ap.getAnalyzingThread();

            while (!ft.isDone()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    t.stop();
                    System.out.println("AnalyzeTextFileTask.call() was interrupted");
                    throw e;
                }
            }

            if (ft.isDone() && !ft.isCancelled()) {
                words = ft.get();
            }
        }

        return words;
    }
}

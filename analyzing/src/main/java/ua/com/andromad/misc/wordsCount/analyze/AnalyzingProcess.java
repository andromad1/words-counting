package ua.com.andromad.misc.wordsCount.analyze;

import java.util.Set;
import java.util.concurrent.FutureTask;

/**
 * This is a wrap for Thread and FutureTask objects that were created for the process of analyzing words in a file.
 * It is intended to return Thread and FutureTask objects to the calling part of the program (or some client) in order
 * to be able to manage the word parsing process (to interrupt the process or get the results).
 */
public class AnalyzingProcess {
    private final Thread analyzingThread;
    private final FutureTask<Set<WordOccurrence>> analyzingTask;

    public AnalyzingProcess(Thread analyzingThread, FutureTask<Set<WordOccurrence>> analyzingTask) {
        this.analyzingThread = analyzingThread;
        this.analyzingTask = analyzingTask;
    }

    public Thread getAnalyzingThread() {
        return analyzingThread;
    }

    public FutureTask<Set<WordOccurrence>> getAnalyzingTask() {
        return analyzingTask;
    }
}

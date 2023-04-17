package ua.com.andromad.misc.wordsCount.analyze;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

/**
 * It is an implementation of the words count algorithm in the file
 */
public class ProcessAnalyzing {
    /* if you want the asynchronous option (to have an opportunity to stop the process) */
    public static AnalyzingProcess startAnalyzeThread(File fileToAnalyze) {
        Callable<Set<WordOccurrence>> callableAnalyzing = () -> startAnalyze(fileToAnalyze);
        FutureTask<Set<WordOccurrence>> futureTaskAnalyzing = new FutureTask<>(callableAnalyzing);
        Thread analyzingThread = new Thread(futureTaskAnalyzing);
        analyzingThread.start();

        return new AnalyzingProcess(analyzingThread, futureTaskAnalyzing);
    }

    /* for testing aims only */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("specify the path to the file");
        } else {
            if (args.length == 1) { // for testing synchronous calling
                System.out.println(startAnalyze(new File(args[0])));
            } else { // for testing asynchronous calling
                AnalyzingProcess ap = startAnalyzeThread(new File(args[0]));
                FutureTask<Set<WordOccurrence>> ft = ap.getAnalyzingTask();

                Scanner in = new Scanner(System.in);
                System.out.println("input STOP to stop analyzing if you want to break analyzing before finishing:");

                while (!ft.isDone()) {
                    String op = in.nextLine();
                    if (op.equals("STOP")) {
                        System.out.println("try stopping");
                        ap.getAnalyzingThread().stop();
                        break;
                    }
                }

                if (ft.isDone() && !ft.isCancelled()) {
                    Set<WordOccurrence> res = new TreeSet<>();
                    try {
                        res = ft.get();
                    } catch (InterruptedException e) {
                        System.out.println("the task was interrupted");
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        System.out.println("the task had ExecutionException");
                        e.printStackTrace();
                    }
                    System.out.println(res);
                }

                System.out.println("check the result");

                while (true) {

                }
            }
        }
    }

    /* if you are satisfied with the synchronous option */
    public static Set<WordOccurrence> startAnalyze(File fileToAnalyze) throws IOException {
        Thread.currentThread().setName("words-count-analyze-thread-"+Thread.currentThread().getId());

        Set<WordOccurrence> words = new ConcurrentSkipListSet<>();

        System.out.println("start reading file");
        var contents = Files.readString(fileToAnalyze.toPath());
        List<String> lstWords = List.of(contents.split("\\p{javaWhitespace}+"));

        System.out.println("start analyzing");

        lstWords.parallelStream().collect(Collectors.groupingByConcurrent(s -> s, Collectors.counting()))
                .forEach((s, cnt) -> words.add(new WordOccurrence(s, cnt)));

        System.out.println("finish analyzing");

        return words;
    }
}

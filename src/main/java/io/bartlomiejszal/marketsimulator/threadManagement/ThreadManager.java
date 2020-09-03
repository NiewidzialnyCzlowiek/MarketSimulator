package io.bartlomiejszal.marketsimulator.threadManagement;

import io.bartlomiejszal.marketsimulator.controllers.MainPageController;
import io.bartlomiejszal.marketsimulator.dataManagement.DataContext;

import java.util.ArrayList;
import java.util.List;

public class ThreadManager {
    private static List<Thread> threads = new ArrayList<>();
    private static boolean runThreads = true;

    public static boolean isRunThreads() {
        return runThreads;
    }

    public static void addAndStartThread(Thread thread) {
        threads.add(thread);
        thread.start();
    }

    public static void addThread(Thread thread) {
        threads.add(thread);
    }

    public static void safelyStopAllThreads() {
        runThreads = false;
        waitForAllThreadsToJoin();
        threads = new ArrayList<>();
    }

    private static void waitForAllThreadsToJoin() {
        threads.forEach(rec -> {
            try {
                rec.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static void runAllAvailableThreads() {
        runThreads = true;
        if (MainPageController.dataContext != null) {
            DataContext _context = MainPageController.dataContext;
            _context.getCustomers().forEach(rec -> addAndStartThread(new Thread((Runnable) rec)));
            _context.getCompanies().forEach(rec -> addAndStartThread(new Thread(rec)));
        }
    }

    public static void setRunThreads(boolean runThreads) {
        ThreadManager.runThreads = runThreads;
    }
}

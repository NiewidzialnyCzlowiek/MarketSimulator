package dataManagement.services;

import Models.assets.Asset;
import dataManagement.DataContext;
import threadManagement.ThreadManager;

public class HistoryRecorder implements Runnable {
    private DataContext _context;

    public HistoryRecorder(DataContext _context) {
        this._context = _context;
    }

    @Override
    public void run() {
        while (ThreadManager.isRunThreads()) {
            try {
                Thread.sleep(1000);
                _context.getAssets().forEach(Asset::recordValueInHistory);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

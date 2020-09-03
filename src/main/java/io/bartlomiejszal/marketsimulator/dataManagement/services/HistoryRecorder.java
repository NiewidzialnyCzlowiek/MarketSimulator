package io.bartlomiejszal.marketsimulator.dataManagement.services;

import io.bartlomiejszal.marketsimulator.model.assets.Asset;
import io.bartlomiejszal.marketsimulator.dataManagement.DataContext;
import io.bartlomiejszal.marketsimulator.threadManagement.ThreadManager;

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

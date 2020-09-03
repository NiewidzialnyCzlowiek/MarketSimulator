package io.bartlomiejszal.marketsimulator.setup;

public class UIDGenerator {
    private static volatile int CurrentUID = 0;
    public static synchronized int getUID() {
        CurrentUID += 1;
        return CurrentUID;
    }

    public static void setCurrentUID(int currentUID) {
        CurrentUID = currentUID;
    }
}

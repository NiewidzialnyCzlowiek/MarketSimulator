package dataManagement.services;

import controllers.MainPageController;
import dataManagement.DataContext;
import threadManagement.ThreadManager;

import java.io.*;

public class DataSerializationService {
    public static boolean serializeDataContext() {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("DatabaseBackup.ser");
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            ThreadManager.safelyStopAllThreads();
            if (MainPageController.dataContext != null) {
                MainPageController.dataContext.backupLastUID();
                objectOutputStream.writeObject(MainPageController.dataContext);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Cannot find file to serialize");
        } catch (IOException e) {
            System.out.println("IO Exception during serialization process");
            e.printStackTrace();
        } finally {
            if ((fileOutputStream != null) || (objectOutputStream != null)) {
                try {
                    objectOutputStream.close();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("Unexpected exception during closing output streams");
                }
            }
        }
        return true;
    }

    public static boolean deserializeDataContext() {
        File file = new File("DatabaseBackup.ser");
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            ObjectInputStream objectInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                objectInputStream = new ObjectInputStream(fileInputStream);
                ThreadManager.safelyStopAllThreads();
                MainPageController.dataContext = (DataContext) objectInputStream.readObject();
                ThreadManager.runAllAvailableThreads();
            } catch (FileNotFoundException e) {
                System.out.println("Cannot find file to deserialize");
            } catch (IOException e) {
                System.out.println("IO Exception during deserialization process");
            } catch (ClassNotFoundException e) {
                System.out.println("Cannot assign imported data to application Data Context");
            } finally {
                if ((fileInputStream != null) || (objectInputStream != null))
                    try {
                        objectInputStream.close();
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        System.out.println("Unexpected exception during closing input streams");
                    }
            }
            return true;
        }
        return false;
    }
}

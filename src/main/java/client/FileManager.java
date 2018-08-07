package client;

import java.io.File;

public class FileManager {

    private String localPath;

    public FileManager() {
        localPath = "src/main/resources/";
    }

    public void setLocalPath(String localPath) {this.localPath = localPath;}
    public String getLocalPath() {return localPath;}

    public void displayLocal() {
        File folder = new File(localPath);

        System.out.print("Directory contents: ");

        File[] files = folder.listFiles();

        for (int i = 0; i < files.length; i++) {
            System.out.print(files[i].getName() + " ");
        }
    }

    // Rename a file on a local server.
    // Return true if successful.
    public boolean renameFileLS(String toRename, String newName) {
        File folder = new File(localPath);

        // Get a list of files.
        File [] files = folder.listFiles();

        // Search all files in the local path.
        // If a file with the name is detected, it will rename it.
        for (int i = 0; i < files.length; i++) {
            if ((files[i].getName().equals(toRename))) {
                File old = new File(files[i].getName());
                File renamed = new File(newName);
                old.renameTo(renamed);
                System.out.print("Success! File " + toRename + " renamed to " + newName + "!");
                return true;
            }
        }

        System.out.println("Error: Could not rename file.");
        return false;

    }
}

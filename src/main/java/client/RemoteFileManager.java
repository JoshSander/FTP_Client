package client;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RemoteFileManager {

    private FTPClient ftp;


    public RemoteFileManager(FTPClient ftp)
    {
        this.ftp = ftp;
    }

    public void displayFiles()
    {
        try
        {
            List<String> files = getFiles();
            files.stream().forEach(System.out::println);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void displayDirectories()
    {
        try
        {
            List<String> files = getDirectories();
            files.stream().forEach(System.out::println);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public List<String> getFiles() throws IOException
    {
            FTPFile[] files  = ftp.listFiles();
            return Arrays.stream(files).filter(f->f.isFile())
                    .map(FTPFile::getName)
                    .collect(Collectors.toList());
    }

    public List<String> getDirectories() throws IOException
    {
        FTPFile[] files  = ftp.listDirectories();
        return Arrays.stream(files)
                .map(FTPFile::getName)
                .collect(Collectors.toList());
    }


    public boolean renameFile(String from, String to)
    {
        try {
            return ftp.rename(from, to);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Returns true if successfully completed, otherwise false
    // IOException also catches FTPConnectionClosedException (if FTP connection closes unexpectedly)
    public boolean removeFile(String pathname) {
        try {
            return ftp.deleteFile(pathname);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean uploadFile(File fileToUpload, String destPath){
        try
        {
             return ftp.storeFile(destPath, new FileInputStream(fileToUpload));
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }


    public boolean makeDirectory(String pathname){
        try {
            return ftp.makeDirectory(pathname);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }
    public boolean downloadFile(String sourcePath, String destPath)
    {
        File downloadedFile = new File(destPath);
        try
        {
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadedFile));
            boolean success = ftp.retrieveFile(sourcePath, outputStream);
            if(!success)
            {
                downloadedFile.delete();
            }
            outputStream.close();
            return success;
        }
        catch (IOException ex)
        {
            downloadedFile.delete();
            return false;
        }

    }

    public boolean downloadMultipleFiles(String sourceFolder, List<String> fileNames, String destFolder) {
        boolean result = true;

        if (fileNames == null || fileNames.size() == 0) {
            return false;
        }

        if (sourceFolder == null || destFolder == null) {
            return false;
        }

        for (String fileName : fileNames) {
            if (!downloadFile(sourceFolder + fileName, destFolder + fileName)) {
                result = false;
            }
        }

        return result;
    }

    public boolean uploadMultipleFiles (List<File> filesToUpload, String destFolder) {
        boolean result = true;

        if (filesToUpload == null || filesToUpload.size() == 0) {
            return false;
        }

        for (File file : filesToUpload) {

            if (uploadFile(file, destFolder + file.getName()) == false) {
                result = false;
            }
        }

        return result;
    }
}



import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileDupeRemover {
    public static void main(String[] args) throws IOException, InvalidTypeException {

        String searchDirectory;

        GetUserInput getInput = new GetUserInput();

        System.out.println("Give an existing directory to search for duplicate files:");
        searchDirectory = getInput.requestInput("dirExist");

        File folder = new File(searchDirectory);

        File backupFolder = (folder.getParentFile().toPath().resolve("FDR-Backup")).toFile();
        if (!backupFolder.exists()) {
            System.out.println("Creating backup folder...");
            try {
                backupFolder.mkdir();
                System.out.println("Success!");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }

        File[] fList = folder.listFiles();
        List<File> queue = new ArrayList<>(Arrays.asList(fList));
        List<FileInfo> files = new ArrayList<>();

        while (!queue.isEmpty()) {
            File a = queue.remove(0);
            if (a.isFile()) {
                files.add(new FileInfo(a));
            } else if (a.isDirectory()) {
                queue.addAll(Arrays.asList(a.listFiles()));
            }
        }

        List<FileInfo> checkedFiles = new ArrayList<>();
        List<Conflicts> conflictSets = new ArrayList<>();

        for (FileInfo curFile : files) {

            for (int i = 0; i < checkedFiles.size(); i++) {
                if (checkedFiles.get(i).getMd5().equals(curFile.getMd5()) && checkedFiles.get(i).getSizeKB() == curFile.getSizeKB()) {
                    if (conflictSets.size() == 0) {
                        conflictSets.add(new Conflicts(checkedFiles.get(i), curFile));
                    } else {
                        boolean found = false;
                        for (Conflicts a : conflictSets) {
                            if (a.getConflictMd5().equals(curFile.getMd5())) {
                                a.addConflictFile(curFile);
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            conflictSets.add(new Conflicts(checkedFiles.get(i), curFile));

                        }
                    }
                }
            }
            checkedFiles.add(curFile);
        }
        System.out.println("Scan complete. Listing duplicates found...");
        int conflictNum = 1;
        for (Conflicts a : conflictSets) {
            System.out.println("~~~Conflict " + (conflictNum) + "~~~");
            int fileNum = 1;
            for(FileInfo fi : a.getConflictFiles()){
                System.out.println("--File " + (fileNum) + "--");
                System.out.println("Name: " + fi.getName() + " Size: " + Math.round(fi.getSizeKB()) + "KB");
                System.out.println("Last Modified: " + fi.getLastModified());
                System.out.println("Path: " + fi.getFilePath());
                fileNum++;
            }
            System.out.println("What file do you want to keep? (Give the number, type 'k' for keep all, or type 'exit' to exit):");
            String userInput = getInput.requestInput("string").toLowerCase();
            boolean success = false;
            while (!success){
                if (userInput.equals("k")){
                    a.keepAllFiles();
                    success = true;
                }
                else if (userInput.equals("exit")){
                    System.exit(0);
                }
                else {
                    try {
                        int num = Integer.parseInt(userInput);
                        if (num <= a.getConflictFiles().size()){
                            a.setFileToKeepNum(num - 1);
                            success = true;
                        }
                        else {
                            System.out.println("Improper input. Try again.");
                            userInput = getInput.requestInput("string").toLowerCase();
                        }
                    }
                    catch (NumberFormatException nfe)
                    {
                        System.out.println("Improper input. Try again.");
                        userInput = getInput.requestInput("string").toLowerCase();
                    }
                }
            }
            conflictNum++;
        }
        for (Conflicts a : conflictSets){
            if(!a.keepAllFilesBool()){
                int keepNum = a.getFileToKeepNum();
                for (int num = 0; num < a.getConflictFiles().size(); num++){
                    if (num != keepNum){
                        File fileToMove = a.getConflictFiles().get(num).getFile();
                        File fileMoveLocation = (backupFolder.toPath().resolve(fileToMove.getName())).toFile();
                        fileToMove.renameTo(fileMoveLocation);
                    }
                }
            }
        }
        if(conflictSets.size() > 0) {
            System.out.println("Program ran successfully! Check \"" + backupFolder.getCanonicalPath() + "\" if there's any deleted files you want back.");
        }
        else {
            System.out.println("No duplicates were found. Exiting...");
        }
    }
}

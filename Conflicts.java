import java.util.ArrayList;
import java.util.List;

public class Conflicts {
    private List<FileInfo> conflictFiles = new ArrayList<>();
    private String conflictMd5;
    private int fileToKeepNum;
    private boolean keep = false;

    public Conflicts(FileInfo a, FileInfo b) {
        this.conflictFiles.add(a);
        this.conflictFiles.add(b);
        this.conflictMd5 = a.getMd5();
    }
    public void addConflictFile(FileInfo a) {
        conflictFiles.add(a);
    }
    public List<FileInfo> getConflictFiles(){
        return conflictFiles;
    }
    public String getConflictMd5(){
        return conflictMd5;
    }
    public void setFileToKeepNum(int i){ this.fileToKeepNum = i; }
    public int getFileToKeepNum(){ return this.fileToKeepNum; }
    public void keepAllFiles(){ this.keep = true; }
    public boolean keepAllFilesBool(){ return keep; }
}

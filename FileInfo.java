import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

public class FileInfo {
    private String md5;
    private String fileExt;
    private File file;
    private double sizeBytes;
    private double sizeKilobytes;
    private double sizeMegabytes;
    private String fileName;
    private Date lastModified;
    private String filePath;


    public FileInfo(File file) throws IOException {
        this.file = file;
        this.filePath = file.getAbsolutePath();

        try (FileInputStream fis = new FileInputStream(file)) {
            this.md5 = DigestUtils.md5Hex(fis);
        }

        this.fileName = file.getName();
        int i = this.fileName.lastIndexOf('.');

        if (i < this.fileName.length()) {
            fileExt = this.fileName.substring(i + 1).toLowerCase();
        }

        this.sizeBytes = file.length();
        this.sizeKilobytes = (sizeBytes / 1024);
        this.sizeMegabytes = (sizeKilobytes / 1024);

        this.lastModified = new Date(this.file.lastModified());
    }

    public String getMd5() {
        return this.md5;
    }

    public File getFile() {
        return this.file;
    }

    public String getFileExt() {
        return this.fileExt;
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    public double getSizeKB() {
        return this.sizeKilobytes;
    }

    public String getName() {
        return this.fileName;
    }

    public String getFilePath() {
        return this.filePath;
    }
}
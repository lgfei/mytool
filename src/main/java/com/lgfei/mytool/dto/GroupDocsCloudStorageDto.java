package com.lgfei.mytool.dto;

import java.io.File;
import java.io.Serializable;

public class GroupDocsCloudStorageDto implements Serializable {
    private String downloadFileName;
    private File downloadFile;

    public String getDownloadFileName() {
        return downloadFileName;
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    public File getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(File downloadFile) {
        this.downloadFile = downloadFile;
    }
}

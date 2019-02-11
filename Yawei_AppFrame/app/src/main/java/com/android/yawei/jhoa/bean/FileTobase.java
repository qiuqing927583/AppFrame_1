package com.android.yawei.jhoa.bean;

/**
 * Created by Yusz on 2018-4-17.
 */

public class FileTobase {

    private String attUrl;
    private String attFileNameString;
    private String fileData;
    private String fileSize;

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    public String getAttUrl() {
        return attUrl;
    }

    public void setAttUrl(String attUrl) {
        this.attUrl = attUrl;
    }

    public String getAttFileNameString() {
        return attFileNameString;
    }

    public void setAttFileNameString(String attFileNameString) {
        this.attFileNameString = attFileNameString;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}

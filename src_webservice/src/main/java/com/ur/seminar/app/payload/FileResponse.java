package com.ur.seminar.app.payload;

/**
 * This class represents the JSON format of an API return
 * **/

public class FileResponse {

    private String fileName;

    /**
     * @param fileName the name of file to include in an API response
     * **/
    public FileResponse(String fileName) {
        this.fileName = fileName;

    }

    public String getFileName() {
        return this.fileName;
    }
}

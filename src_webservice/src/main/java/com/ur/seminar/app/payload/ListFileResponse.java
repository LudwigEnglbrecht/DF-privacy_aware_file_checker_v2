package com.ur.seminar.app.payload;

import java.util.ArrayList;

/**
 * This class represents the JSON format of the ListFiles APIs
 * **/
public class ListFileResponse {

    private ArrayList<String> fileNames;

    public ListFileResponse(ArrayList<String> fileNames){

        this.fileNames = fileNames;
    }

    public ArrayList<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(ArrayList<String> fileNames) {
        this.fileNames = fileNames;
    }
}

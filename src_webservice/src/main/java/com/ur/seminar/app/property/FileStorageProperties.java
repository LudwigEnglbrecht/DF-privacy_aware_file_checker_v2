package com.ur.seminar.app.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * This class is a model for the Configuration Properties used by Spring to configure the storage locations
 * These locations come from the application.properties resource class
 * */
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String scripts;
    private String upload;
    private String download;
    private String directory;
    private String errors;

    public String getErrors(){return errors;}

    public void setErrors(String errors){ this.errors = errors;}

    public void setScripts(String scripts){ this.scripts = scripts; }

    public String getScripts() { return scripts;}
    /**Getter of the directory location path
     * @return the directory location path
     * */
    public String getDirectory() {
        return directory;
    }
    /**Setter of the directory location path
     * @param directory path defined in the application.properties file
     *  */
    public void setDirectory(String directory) {
        this.directory = directory;
    }
    /**Getter of the upload location path
     * @return the upload location path
     * */
    public String getUpload() {
        return upload;
    }
    /**Getter of the download location path
     * @return the download location path
     * */
    public String getDownload() {
        return download;
    }
    /**Setter of the upload location path
     * @param upload path defined in the application.properties file
     * */
    public void setUpload(String upload) {
        this.upload = upload;
    }
    /**Setter of the download location path
     * @param download path defined in the application.properties file
     * */
    public void setDownload(String download) {
        this.download = download;
    }
}


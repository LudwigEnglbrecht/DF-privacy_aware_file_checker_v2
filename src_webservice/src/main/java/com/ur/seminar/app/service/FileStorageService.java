package com.ur.seminar.app.service;

import com.ur.seminar.app.exception.FileStorageException;
import com.ur.seminar.app.exception.MyDirectoryNotFoundException;
import com.ur.seminar.app.exception.MyFileNotFoundException;
import com.ur.seminar.app.property.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


/**
 * This class contains the logic of storing and downloading a file from the file system
 * It is used in some methods of the FileController class
 * @see com.ur.seminar.app.controller.FileController
 * */
@Service
public class FileStorageService {

    private final Path scriptLocation;
    private final Path directoryLocation;
    private final Path fileUploadLocation;
    private final Path fileDownloadLocation;


    int count;
    /**
     * Constructor of the class
     * @param fileStorageProperties object of the FileStorageProperties class containing different paths
     * */
    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties){
        this.fileUploadLocation = Paths.get(fileStorageProperties.getUpload())
                .toAbsolutePath().normalize();
        this.fileDownloadLocation = Paths.get(fileStorageProperties.getDownload()).toAbsolutePath().normalize();
        this.directoryLocation = Paths.get(fileStorageProperties.getDirectory()).toAbsolutePath().normalize();
        this.scriptLocation = Paths.get(fileStorageProperties.getScripts()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileUploadLocation);
            Files.createDirectories(this.fileDownloadLocation);
            Files.createDirectories(this.directoryLocation);
            Files.createDirectories(this.scriptLocation);

        } catch (Exception ex){
            throw new FileStorageException("Could not create directory where the uploaded file will be stored.", ex);
        }
    }

    /**
     * Loads a file from a directory
     * @param fileName the name of the file to load
     * @param directory the name of a directory
     * @return a file packed into a Resource object*/
    public Resource loadFileAsResource(String fileName, String directory){
        try {
            switch (directory) {
                case "results": {
                    Path filePath = this.fileDownloadLocation.resolve(fileName).normalize();
                    Resource resource = new UrlResource(filePath.toUri());
                    if (resource.exists()) {
                        return resource;
                    } else {
                        throw new MyFileNotFoundException("File not found " + fileName);
                    }
                }
                case "uploads": {
                    Path filePath = this.fileUploadLocation.resolve(fileName).normalize();
                   Resource resource = new UrlResource(filePath.toUri());
                    if (resource.exists()) {
                        return resource;
                    } else {
                        throw new MyFileNotFoundException("File not found " + fileName);
                    }
                }
                case "directory": {
                    Path filePath = this.directoryLocation.resolve(fileName).normalize();
                    Resource resource = new UrlResource(filePath.toUri());
                    if (resource.exists()) {
                        return resource;
                    } else {
                        throw new MyFileNotFoundException("File not found " + fileName);
                    }
                }
                case "scripts": {
                    Path filePath = this.scriptLocation.resolve(fileName).normalize();
                    Resource resource = new UrlResource(filePath.toUri());
                    if (resource.exists()) {
                        return resource;
                    }
                    break;
                }
                case "Zips": {
                    Path filePath = Paths.get(".\\Zips\\" + fileName);
                    Resource resource = new UrlResource(filePath.toUri());
                    if (resource.exists()) {
                        return resource;
                    }
                }
                default:
                    throw new MyDirectoryNotFoundException("Directory not found");

            }
            Path filePath = Paths.get(".\\error\\directoryNotFound.txt");
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException ex){
            throw new MyFileNotFoundException("File not found " +  fileName, ex);
        }

    }

    /**
     * This Service method saves a file into the Script-Directory
     * @param file a file to add to the Script-Directory
     * @return the name of the added script
     * **/
    public String addScript(MultipartFile file){

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")){
                throw new FileStorageException("Sorry! Filename contains invalid path sequence" + fileName);
            }

        Path targetLocation = this.scriptLocation.resolve(fileName);
        Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
    }catch (IOException ex) {
            throw new FileStorageException("Could not add file " + fileName + "to database!", ex);
        }
    }

    /**
     * This Service method saves a file into the Directory-Directory
     * @param file a file to add to the Directory-Directory
     * @return the name of the added file
     * **/
    public String addToDirectory(MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")){
                throw new FileStorageException("Sorry! Filename contains invalid path sequence" + fileName);
            }

            Path targetLocation = this.directoryLocation.resolve(fileName);
            Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex){
            throw new FileStorageException("Could not add file " + fileName + "to database!", ex);
        }
    }

    /**
     * This Service method saves a file into the Uploads-Directory
     * @param file a file to add to the Uploads-Directory
     * @return the name of the added file
     * **/
    public String storeFile(MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")){
                throw new FileStorageException("Sorry! Filename contains invalid path sequence" + fileName);
            }

            Path targetLocation = this.fileUploadLocation.resolve(fileName);
            Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex){
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * Deletes all entries from the results directory
     * @return the number of deleted files
     **/
    public int clearResult(){
        int count = 0;
        File results = new File(String.valueOf(fileDownloadLocation));
        File[] files = results.listFiles();
        if(files != null){
            count += files.length;
            for (File file : files) {
                file.delete();
        }
        }
        File zip = new File(".\\Zips\\Results.zip");
        zip.delete();
        return count;
    }

    /**
     * Deletes all entries from the uploads directory
     * @return the number of deleted files
     **/
    public int clearUploads(){
        int count = 0;
        File uploads = new File(String.valueOf(fileUploadLocation));
        File[] files = uploads.listFiles();
        if(files != null){
            count += files.length;
            for(File file : files){
                file.delete();
            }
        }
        File zip = new File(".\\Zips\\Uploads.zip");
        zip.delete();
        return count;
    }
    /**
     * Deletes all entries from the directory directory
     * @return the number of deleted files
     **/
    public int clearDirectory(){
        int count = 0;
        File directory = new File(String.valueOf(directoryLocation));
        File[] files = directory.listFiles();
        if(files != null){
            count += files.length;
            for(File file : files){
                file.delete();
            }
        }
        File zip = new File(".\\Zips\\Directory.zip");
        zip.delete();
        return count;
    }
    /**
     * Deletes a single entry from the directory
     * @param fileName the name of a file to delete
     **/
    public void deleteEntry(String fileName){

        String[] filePlaces = {String.valueOf(directoryLocation),String.valueOf(fileUploadLocation), String.valueOf(scriptLocation),String.valueOf(fileDownloadLocation)};
       for(int x = 0; x < filePlaces.length; x++){
           File databasePath = new File(filePlaces[x]);
           File[] database = databasePath.listFiles();

           if(database != null){
               for(File file : database){
                   if(file.getName().equals(fileName)){
                       file.delete();
                   }
               }
           }
       }
    }
}

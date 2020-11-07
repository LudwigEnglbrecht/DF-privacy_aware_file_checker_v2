package com.ur.seminar.app.controller;

import com.ur.seminar.Main;
import com.ur.seminar.app.Differentiator;
import com.ur.seminar.app.payload.UpdateDirectoryResponse;
import com.ur.seminar.app.payload.FileResponse;
import com.ur.seminar.app.service.FileStorageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@RestController
public class FileController {

    /*
     * Use the following paths to access the mounted Nextcloud directories
     * */
    private static final String mountPathDirectory = "/media/cloud/Content/directory";
    private static final String mountPathUploads = "/media/cloud/Content/uploads";
    private static final String mountPathResults = "/media/cloud/Content/results";
    private static final String mountPathScripts = "/media/cloud/Content/scripts";

    private static final String relativPathUploads = ".\\uploads";
    private static final String relativPathDirectory = ".\\directory";
    private static final String relativPathResults = ".\\results";
    private static final String relativPathScripts = ".\\scripts";

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    /**
     * @see FileStorageService for information about the Service class
     */
    @Autowired
    private FileStorageService fileStorageService;

    /**
     *This API-Method loads and returns the Uploads Zip-File as a Base64 encoded String
     * @param request represents the Http-Request
     * @return return the Base64 String of the packaged Zip File
     * @throws IOException throws an exception if there is no Uploads.zip file
     * **/
    @GetMapping("/getUploads")
    public String getUploads(HttpServletRequest request) throws IOException {

        File file = new File(".\\Zips\\Uploads.zip");
        byte[] bytesArray = new byte[(int) file.length()];

        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray);
        fis.close();

        String encodedString = Base64.getEncoder().encodeToString(bytesArray);

        return encodedString;
    }
    /**
     * This API-Method loads and returns the Directory Zip-File as a Base64 encoded String
     * @param request represents the Http-Request
     * @return the Zip-File as a Base64 String
     * @throws IOException throws an exception if there is no Directory.zip file
     * **/
    @GetMapping("/getDirectory")
    public String getDirectory(HttpServletRequest request) throws IOException {

        File file = new File(".\\Zips\\Directory.zip");
        byte[] bytesArray = new byte[(int) file.length()];

        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray);
        fis.close();

        String encodedString = Base64.getEncoder().encodeToString(bytesArray);

        return encodedString;
    }
    /**
     * This API-Method loads and returns the Directory Zip-File as a Base64 encoded String
     * @param request represents the Http-Request
     * @return the Zip-File as a Base64 String
     * @throws IOException throws an exception if there is no Results.zip file
     * **/
    @GetMapping("/getResults")
    public String getResults(HttpServletRequest request) throws IOException {

        File file = new File(".\\Zips\\Results.zip");
        byte[] bytesArray = new byte[(int) file.length()];

        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray);
        fis.close();

        String encodedString = Base64.getEncoder().encodeToString(bytesArray);

        return encodedString;
    }
    /**
     * This API-Method loads a single Result-File using the FilStorageService
     * @param fileName the Name of the Result-File
     * @param request represents the Http-Request as a parameter
     * @return the Result-File as a Ressource
     * **/
    @GetMapping("/getResult/{fileName:.+}")
    public ResponseEntity<Resource> getResult(@PathVariable String fileName, HttpServletRequest request) {

        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName, "results");


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    /**
     * This API-Method lists and returns all files in the Script-Directory
     * @return a List of the names of the files inside the Script-Directory
     * @throws IOException throws an exception if there is no directory
     * **/
    @GetMapping("/getScriptList")
    public List<String> getScriptList() throws IOException {

        List<String> fileNames;

        fileNames = Files.list(Paths.get(mountPathScripts))
                .filter(Files::isRegularFile)
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
        return fileNames;
    }

    /**
     * This API-Method lists and returns all files in the Directory-Directory
     * @return a List of the names of the files inside the Directory-Directory
     * @see FileStorageService
     * @throws IOException throws an exception if there is no directory
     * **/
    @GetMapping("/getDirectoryList")
    public List<String> getDirectoryList() throws IOException {

        List<String> fileNames;

        ArrayList<Resource> directory = new ArrayList<>();

        fileNames = getFileNames("directory");

        for (String fileName : fileNames) {
            System.out.println(fileStorageService.loadFileAsResource(fileName, "directory"));
            directory.add(fileStorageService.loadFileAsResource(fileName, "directory"));
        }
        if (directory.size() > 0) {
            zipFiles(directory, "Directory");
        }
        return fileNames;
    }

    /**
     * This API-Method lists and returns all files in the Results-Directory
     * @return a List of the names of the files inside the Results-Directory
     * @see FileStorageService
     * @throws IOException throws an exception if there is no directory
     * **/
    @GetMapping("/getResultList")
    public List<String> getResultList() throws IOException {

        List<String> fileNames;

        ArrayList<Resource> results = new ArrayList<>();

        fileNames = getFileNames("results");

        for (String fileName : fileNames) {
            System.out.println(fileStorageService.loadFileAsResource(fileName, "results"));
            results.add(fileStorageService.loadFileAsResource(fileName, "results"));
        }
        if (results.size() > 0) {
            zipFiles(results, "Results");
        }

        return fileNames;
    }

    /**
     * This API-Method lists and returns all files in the Upload-Directory
     * @return a List of the names of the files inside the Upload-Directory
     * @see FileStorageService
     * @throws IOException throws an exception if there is no directory
     * **/
    @GetMapping("/getUploadList")
    public List<String> getUploadList() throws IOException {

        List<String> fileNames;

        ArrayList<Resource> uploads = new ArrayList<>();

        fileNames = getFileNames("uploads");

        for (String fileName : fileNames) {
            System.out.println(fileStorageService.loadFileAsResource(fileName, "uploads"));
            uploads.add(fileStorageService.loadFileAsResource(fileName, "uploads"));
        }
        if (uploads.size() > 0) {
            zipFiles(uploads, "Uploads");
        }

        return fileNames;

    }

    /**
     * This API-Method represents the External-Call function of the Prototype,
     * The method starts an external call with a linux command in order to execute a python script.
     * @param fileName the name of a script file
     * @param goal the name of file to combine with the script
     * @return a Response with a confirmation of the successful call
     * **/
    @PostMapping("/externalCall")
    public FileResponse makeExternalCall(@RequestParam("fileName") String fileName, String goal) {

        try {
            Process process = Runtime.getRuntime().exec("python /media/cloud/Content/scripts/" + fileName + " /media/cloud/Content/uploads/" + goal + " > /media/cloud/Content/results/output_script");
            //Process process = Runtime.getRuntime().exec("echo //media//cloud//Content//scripts//peepdf-master//peepdf.py -f -l  //media//cloud//Content//uploads//AblaufplanSS2020.pdf > //media//cloud//Content//results//output_script.txt");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(process.getErrorStream()));
            String s = null;
            String csvLine = "";

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                csvLine = s;
            }

            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }


        } catch (IOException io) {
            io.printStackTrace();
            return new FileResponse("Couldnt find the Phython Skript! Please try again.");
        }

        return new FileResponse("Successfully called " + fileName);

    }

    /**
     * This API-Method uses the FileStorageService to add a file into the directory
     * @param file the file to add to the Directory
     * @return an instance of the UpdateDirectoryResponse class
     * @see UpdateDirectoryResponse
     * @see FileStorageService
     * **/
    @PostMapping("/addToDirectory")
    public UpdateDirectoryResponse addToDirectory(@RequestParam("file") MultipartFile file) {

        String fileName = fileStorageService.addToDirectory(file);

        int databaseCount;
        //File databaseDirectory = new File( "C:/Users/Dan/Documents/Wirtschaftsinformatik/Semester_6/bachelor/media/cloud/Content/directory");
        File databaseDirectory = new File(mountPathDirectory);

        if (databaseDirectory.listFiles() != null) {

            databaseCount = Objects.requireNonNull(databaseDirectory.listFiles()).length;

        } else {
            databaseCount = 0;
        }


        return new UpdateDirectoryResponse(fileName,
                file.getContentType(), file.getSize(), databaseCount);
    }

    /**
     * This API-Method uses the addToDirectory-method
     * @param files a list of files to add with the addToDirectory-Method
     * @return a list of DirectoryResponses
     * **/
    @PostMapping("/addMultipleToDirectory")
    public List<UpdateDirectoryResponse> addMultipleToDirectory(@RequestParam("files") MultipartFile[] files) {

        return Arrays.stream(files)
                .map(this::addToDirectory)
                .collect(Collectors.toList());
    }

    /**
     * This API-Method uses the FileStorageService to add a script to the Script-Directory
     * @param file a python script file to add to the script-directory
     * @return a statement to confirm the successful script call
     * @see FileStorageService
     * **/
    @PostMapping("/uploadScript")
    public FileResponse uploadScript(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.addScript(file);
        return new FileResponse("Sucessfully called the "+ fileName+". Check the results-directory");
    }

    /**
     * This API-Method uses the FileStorageService to add a script tp the Uploads-Directory
     * @param file a file to add to the Uploads-Directory
     * @return a statement to confirm the successful upload
     * @see FileStorageService
     * **/
    @PostMapping("/uploadFile")
    public FileResponse uploadFile(@RequestParam("file") MultipartFile file) {

        String fileName = fileStorageService.storeFile(file);

        return new FileResponse(fileName);
    }

    /**
     * This API-Method uses the uploadFile-method to add a list of files to the Upload-Directory
     * @param files a list of files to add to the Uploads-Directory
     * @return a list of statements to confirm the successful uploads
     * **/
    @PostMapping("/uploadMultipleFiles")
    public List<FileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {

        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }

    /**
     * This API-Method uses the FileStorageService to clear a directory
     * @param directoryName the name of a directory (uploads, directory, results)
     * @return the number of deleted files of the selected directory
     * @see FileStorageService
     * **/
    @DeleteMapping("/clearDirectory")
    public ResponseEntity<?> clearDirectory(@RequestParam("directoryName") String directoryName) {
        int counter = 0;
        if (directoryName.equals("uploads")) {
            counter = fileStorageService.clearUploads();
        }
        if (directoryName.equals("directory")) {
            counter = fileStorageService.clearDirectory();
        }
        if (directoryName.equals("results")) {
            counter = fileStorageService.clearResult();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.ACCEPT)
                .body("Directory " + directoryName + " was cleared and " + counter + " files were deleted!");
    }

    /**
     * This API-Method uses the FileStorageService to delete a file from the directory
     * @param fileName a file to delete from the directory
     * @return confirms the deleted file
     * @see FileStorageService
     * **/
    @DeleteMapping("/deleteEntry/{fileName:.+}")
    public ResponseEntity<?> deleteEntry(@PathVariable String fileName) {

        fileStorageService.deleteEntry(fileName);
        return ResponseEntity.ok().header(HttpHeaders.ACCEPT)
                .body(fileName + " was deleted from the directory");

    }

    /**
     * This API-Method represents the FileChecker-function of the prototype.
     * It uses the Differentiator class to calculate the entropy of a file and save the calculation in result file
     * @param fileName the name of a file of the Uploads-Directory to calculate
     * @return a confirmation of the successful calculation
     * **/
    @PostMapping("/fileChecker")
    public FileResponse fileChecker(@RequestParam("fileName") String fileName) {

        int counter = 0;

        File inputPath = new File(mountPathUploads + "/" + fileName);

        String outputName;

        //instantiating the Differentiator class and creating the output
        try {
            Differentiator differentiator = new Differentiator(inputPath, fileName);
            outputName = differentiator.getOutputName();
            System.out.println(outputName);


        } catch (IOException e) {
            e.printStackTrace();
            outputName = "error";
            return new FileResponse("Your directory is empty!!!");

        }

        return new FileResponse(outputName);
    }

    /**
     * This method loads and return the names of the content of a directory
     * It is used in the API-Methods, that return the fileNames
     * @param directory the name of the directory
     * @return a list of String containing the content of a directory
     * **/
    public List<String> getFileNames(String directory) {

        List<String> fileNames = null;

        try {
            switch (directory) {
                case "directory": {
                    fileNames = Files.list(Paths.get(mountPathDirectory))
                            .filter(Files::isRegularFile)
                            .map(p -> p.getFileName().toString())
                            .collect(Collectors.toList());
                    return fileNames;
                }
                case "results": {
                    fileNames = Files.list(Paths.get(mountPathResults))
                            .filter(Files::isRegularFile)
                            .map(p -> p.getFileName().toString())
                            .collect(Collectors.toList());
                    return fileNames;
                }
                case "uploads": {
                    fileNames = Files.list(Paths.get(mountPathUploads))
                            .filter(Files::isRegularFile)
                            .map(p -> p.getFileName().toString())
                            .collect(Collectors.toList());
                    return fileNames;
                }
                case "scripts": {
                    fileNames = Files.list(Paths.get(mountPathScripts))
                            .filter(Files::isRegularFile)
                            .map(p -> p.getFileName().toString())
                            .collect(Collectors.toList());
                    return fileNames;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileNames;

    }

    /**
     * This method packs the files into a Zip-File and saves it into a file in the Zips folder
     * @param list a list of files
     * @param directory the name of a directory to zip
     * @throws IOException if the InputStream is empty
     * **/
    public void zipFiles(ArrayList<Resource> list, String directory) throws IOException {
        FileOutputStream fos = new FileOutputStream(".\\Zips\\" + directory + ".zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (Resource file : list) {
            InputStream fis = file.getInputStream();
            ZipEntry zip = new ZipEntry(file.getFilename());
            zipOut.putNextEntry(zip);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        zipOut.close();
        fos.close();

    }

}



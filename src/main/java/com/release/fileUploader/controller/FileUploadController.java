package com.release.fileUploader.controller;

import com.release.fileUploader.tools.FileTools;
import com.release.fileUploader.tools.IFileTools;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/")
public class FileUploadController {
    protected static Logger logger = Logger.getLogger(FileUploadController.class);
    IFileTools fileTools = new FileTools();


    @Value("${app.upload_folder}")
    private String upload_folder;
    String fileName="";

    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(
            @RequestParam("folder")String folder,
            @RequestParam("issue_no")String issueNumber,
            @RequestParam("ticket_no")String ticketNumber,
            @RequestParam(value = "releaseNameExists", defaultValue = "false")Boolean specifyReleaseName,
            @RequestParam(value = "releaseList", defaultValue = "")String releaseName,
            @RequestParam("file") MultipartFile file){

        fileName="";

        if (folder.toUpperCase().equals("NONE")){
            logger.debug("Folder does not selected!");
            return "Please, choose script(s) category!";
        }
        if (!(issueNumber.isEmpty() && issueNumber!=null) || !(ticketNumber.isEmpty() && ticketNumber!=null)){
            List<String> appendList =  new ArrayList<>();

            if(!(issueNumber.isEmpty() && issueNumber!=null)){
                logger.debug("issueNumber: "+issueNumber);
                appendList.add("Issue"+issueNumber);
            }
            if(!(ticketNumber.isEmpty() && ticketNumber!=null)){
                logger.debug("ticketNumber: "+ticketNumber);
                appendList.add(ticketNumber);
            }
            if(!appendList.isEmpty()){
                fileName=fileTools.fileNameAppender(file.getOriginalFilename().toString(),appendList);
                logger.debug("result fileName: "+fileName);
            }
            else {
                appendList= Collections.emptyList();
            }
        }
        if(specifyReleaseName==true && !releaseName.equals("") && releaseName!=null){
            upload_folder=new StringBuilder().append(upload_folder).append("\\").append(releaseName).append("\\").toString();
        }
        if (!file.isEmpty()) {
            logger.debug("upload_folder: "+upload_folder+" folder: "+folder+" fileName"+fileName+" file(.getOriginalFilename): "+file.getOriginalFilename());
            return fileTools.fileUploader(upload_folder,folder,fileName,file);

        } else {
            return "You failed to upload " + folder + " because the file was empty.";
        }
    }



}

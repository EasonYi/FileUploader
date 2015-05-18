package com.release.fileUploader.tools;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Igor on 15.05.2015.
 */
public class FileTools implements IFileTools {
    protected static Logger logger = Logger.getLogger(FileTools.class);

    @Override
    public void createDirIfEmpty(File filepath) throws SecurityException{
        if(!filepath.exists()){
            filepath.mkdirs();
            logger.debug("create new directory(s)!");
        }
    }

    @Override
    public String fileNameAppender(String originalFileName, List<String> appendix){
        String fileName=FilenameUtils.getBaseName(originalFileName.toString());
        String fileExtension=FilenameUtils.getExtension(originalFileName.toString());
        StringBuilder appender = new StringBuilder();
        for (String value:appendix){
            if(value!=null && !value.isEmpty()){
                appender=appender.append("_").append(value);
            }
        }
        if(appender!=null && !appender.equals("")){
            logger.debug("fileNameAppender: "+ new StringBuilder().append(fileName).append(appender).append(".").append(fileExtension).toString());
            return new StringBuilder().append(fileName).append(appender).append(".").append(fileExtension).toString();
        }
        logger.debug("originalFileName: "+originalFileName);
        return originalFileName;
    }

    @Override
    public String fileUploader(String root, String folder, String filename, MultipartFile file){
        //check filename
        if(filename.isEmpty() || filename.equals("") || filename==null){
            filename=file.getOriginalFilename();
            logger.debug("filename empty, get original file: "+filename);
        }

        createDirIfEmpty(new File(new StringBuilder().append(root).append("\\").append(folder).toString()));

        File filePath= new File(new StringBuilder().append(root).append("\\").append(folder).append("\\").append(filename).toString());

        try {

            byte[] bytes = file.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(filePath));
            stream.write(bytes);
            stream.close();

            return "You successfully uploaded " + file.getOriginalFilename() + "!";
        } catch (Exception e) {
            return "You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage();
        }
    }
}

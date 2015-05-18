package com.release.fileUploader.tools;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * Created by Igor on 15.05.2015.
 */
public interface IFileTools {
    void createDirIfEmpty(File filepath) throws SecurityException;

    String fileNameAppender(String originalFileName, List<String> appendix);

    String fileUploader(String root, String folder, String filename, MultipartFile file);
}

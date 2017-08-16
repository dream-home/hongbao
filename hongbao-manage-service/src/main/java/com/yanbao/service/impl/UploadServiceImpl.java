package com.yanbao.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by summer on 2016-12-12:13:13;
 */
@Service
public class UploadServiceImpl {
    Log log= LogFactory.getLog(this.getClass());

    public String uploadPictureToLocal(MultipartFile multipartFile, String rootDirPath, String relativeDirPath, String fileName) {
        String fullDirPath = rootDirPath + relativeDirPath;
        if (fullDirPath.endsWith("/") || fullDirPath.endsWith("\\")) {
            fullDirPath = fullDirPath.substring(0, fullDirPath.length() - 1);
        }
        checkAndCreateDirectory(fullDirPath);
        final String fullPicturePath = fullDirPath + "/" + fileName;
        final File pictureFile = new File(fullPicturePath);
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            outputStream = new FileOutputStream(pictureFile);
            int readBytes = 0;
            byte[] buffer = new byte[10000];
            while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
                outputStream.write(buffer, 0, readBytes);
            }

        } catch (Exception e) {

            throw new RuntimeException(e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e2) {
            }
        }
        return fullPicturePath;
    }

    private void checkAndCreateDirectory(String directoryPath) {
        final List<File> needToCreateDirectoryList = new ArrayList<File>();
        File file = new File(directoryPath);
        while (file != null && !file.exists()) {
            needToCreateDirectoryList.add(file);
            file = file.getParentFile();
        }
        if (needToCreateDirectoryList.isEmpty()) {
            return;
        }
        for (int i = needToCreateDirectoryList.size() - 1;i >= 0;i--) {
            File f = needToCreateDirectoryList.get(i);
            f.mkdir();
        }
    }

}

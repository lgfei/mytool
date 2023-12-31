package com.lgfei.mytool.util;

import com.lgfei.mytool.exception.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class IOUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtil.class);
    public static void closeIOStream(InputStream in, OutputStream out){
        if(null != in){
            try {
                in.close();
            } catch (IOException e) {
                LOGGER.error("关闭输入流异常：{}", e.getMessage());
            }
        }
        if(null != out){
            try {
                out.close();
            } catch (IOException e) {
                LOGGER.error("关闭输出流异常：{}", e.getMessage());
            }
        }
    }

    // 删除文件夹
    public static void removeDir(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 递归删除子文件夹
                        removeDir(file.getAbsolutePath());
                    } else {
                        // 删除文件
                        file.delete();
                    }
                }
            }
            // 删除空文件夹
            dir.delete();
        }
    }

    public static void downloadFile(String fileUrl, String savePath) throws IOException {
        checkParentDir(savePath);
        URL url = new URL(fileUrl);
        BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(savePath);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer, 0, 1024)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }
        fileOutputStream.close();
        inputStream.close();
    }

    public static void cloneRepository(String repositoryUrl, String codeDir, String currTime) {
        try{
            // 清理旧文件
            removeDir(codeDir);
            // 创建目标文件夹
            String clonePath = codeDir + currTime;
            File cloneDir = new File(clonePath);
            cloneDir.mkdirs();

            // 执行 git clone 命令
            ProcessBuilder processBuilder = new ProcessBuilder("git", "clone", repositoryUrl, clonePath);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new IOException("代码克隆失败！Exit code: " + exitCode);
            }
        }catch (Exception e){
            throw new CommonException("克隆git仓库代码异常",e);
        }
    }

    public static String readFileToStr(String filePath) {
        StringBuilder content = new StringBuilder();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new CommonException("文件内容读取异常", e);
        }finally {
            if( null != reader){
                try {
                    reader.close();
                } catch (IOException e) {
                    LOGGER.error("关闭Reader异常：{}", e.getMessage());
                }
            }
        }

        return content.toString();
    }

    public static void writeStrToFile(String content, String filePath){
        BufferedWriter writer = null;
        try {
            checkParentDir(filePath);
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(content);
        }catch (IOException e){
            throw new CommonException("字符写入文件异常", e);
        }finally {
            if(null != writer){
                try {
                    writer.close();
                } catch (IOException e) {
                    LOGGER.error("关闭Writer异常：{}", e.getMessage());
                }
            }
        }
    }

    private static void checkParentDir(String filePath){
        Path path = Paths.get(filePath);
        Path directory = path.getParent();
        File dir = directory.toFile();
        if(!dir.exists()){
            dir.mkdirs();
        }
    }
}

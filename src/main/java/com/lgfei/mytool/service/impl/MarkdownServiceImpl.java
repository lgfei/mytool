package com.lgfei.mytool.service.impl;

import com.lgfei.mytool.converter.MarkdownConverter;
import com.lgfei.mytool.dto.GroupDocsCloudStorageDto;
import com.lgfei.mytool.exception.CommonException;
import com.lgfei.mytool.service.MarkdownService;
import com.lgfei.mytool.util.DateUtil;
import com.lgfei.mytool.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class MarkdownServiceImpl implements MarkdownService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarkdownServiceImpl.class);
    private final MarkdownConverter converter;

    public MarkdownServiceImpl(MarkdownConverter converter) {
        this.converter = converter;
    }

    private static final int MAX_LEN = 1024 * 1024;
    @Value("${mytool.file.storage.code-mybook:/data/files/mytool/code/mybook/}")
    private String mybookCodeDir;
    @Value("${mytool.file.storage.groupdocs-conversion:/opt/mytool/files/groupdocs-conversion/}")
    private String groupdocsConversionDir;

    /**
     * <p>
     * 1.下载markdown文件
     * 先尝试从 raw.githubusercontent.com 域名直接下载md文件，
     * 如果 raw.githubusercontent.com 不可访问，则克隆整个工程
     * 2.将markdown文件转为html文件
     * 3.将html文件转为想要文件类型
     * </p>
     * @return
     */
    @Override
    public GroupDocsCloudStorageDto generateResume() {
        String currDate = DateUtil.getCurrDate2yyyyMMdd();
        String workDir = groupdocsConversionDir + currDate + File.separator;
        String currTime = DateUtil.getCurrTime2yyyyMMddHHmmss();
        //String mdFileUrl = "https://raw.githubusercontent.com/lgfei/mybook/master/resume/README.md";
        String mdFileUrl = "https://gitee.com/lgfei/mybook/raw/master/resume/README.md";
        String fileName = "lgf_resume_" + currTime;
        String mdFilePath = null;
        // 1.下载markdown文件
        try {
            mdFilePath = workDir + fileName  + ".md";
            LOGGER.info("开始下载md文件:[{}]", mdFileUrl);
            IOUtil.downloadFile(mdFileUrl, mdFilePath);
        } catch (IOException e) {
            LOGGER.warn("md文件下载失败:[{}], 开始克隆git仓库代码", e.getMessage());
            //IOUtil.cloneRepository("https://github.com/lgfei/mybook.git", CODE_DIR, currTime);
            IOUtil.cloneRepository("https://gitee.com/lgfei/mybook.git", mybookCodeDir, currTime);
            mdFilePath = mybookCodeDir + currTime + File.separator + "resume" + File.separator + "README.md";
        }

        // 2.将markdown文件转为html文件
        String html = null;
        try {
            String mdOrgi = IOUtil.readFileToStr(mdFilePath);
            LOGGER.info("读取md文件内容成功");
            String md = MarkdownConverter.handleSpecial(mdOrgi);
            LOGGER.info("特殊内容处理成功");
            html = MarkdownConverter.convertMarkdownToHtml(md);
            LOGGER.info("将markdown语法解析为html成功");
        } catch (Exception e) {
            throw new CommonException("将markdown文件转为html文件失败", e);
        }

        // 3.将html文件转为想要文件类型
        GroupDocsCloudStorageDto resultDto = null;
        try {
            resultDto = converter.convertHtmlToPdf(html, workDir, fileName);
            LOGGER.info("html文件转化为目标文件成功");
            return resultDto;
        } catch (Exception e) {
            throw new CommonException("调用GroupDocsCloud接口失败", e);
        }
    }

    @Override
    public GroupDocsCloudStorageDto toHtml(String md) {
        // 校验
        String mdData = md.trim();
        if(!StringUtils.hasLength(mdData)){
            throw new CommonException("内容不能为空");
        }
        if(mdData.length() > MAX_LEN){
            throw new CommonException("内容超出最大长度限制");
        }
        String currDate = DateUtil.getCurrDate2yyyyMMdd();
        String workDir = groupdocsConversionDir + currDate + File.separator;
        String currTime = DateUtil.getCurrTime2yyyyMMddHHmmss();
        String mdFilePath = workDir + "md_to_html_" +  currTime + ".md";
        if(mdData.startsWith("http")){
            // 如果是文件地址则先下载
            try {
                LOGGER.info("开始下载md文件:[{}]", mdData);
                IOUtil.downloadFile(mdData, mdFilePath);
            } catch (IOException e) {
                throw new CommonException(String.format("md文件[%s]下载失败:[%s]", md, e.getMessage()));
            }
        }else{
            // 将md内容写入临时文件
            IOUtil.writeStrToFile(mdData, mdFilePath);
        }
        LOGGER.info("md文件保存成功");

        String html = null;
        String htmlFileName = "md_to_html_" +  currTime + ".html";
        String htmlFilePath = workDir + htmlFileName;
        try {
            String mdFileData = IOUtil.readFileToStr(mdFilePath);
            LOGGER.info("读取md文件内容成功");
            html = MarkdownConverter.convertMarkdownToHtml(mdFileData);
            LOGGER.info("将markdown语法解析为html成功");
            IOUtil.writeStrToFile(html, htmlFilePath);
            LOGGER.info("html文件保存成功");
        } catch (Exception e) {
            throw new CommonException("将markdown文件转为html文件失败", e);
        }

        GroupDocsCloudStorageDto dto = new GroupDocsCloudStorageDto();
        dto.setDownloadFileName(htmlFileName);
        dto.setDownloadFile(new File(htmlFilePath));

        return dto;
    }
}

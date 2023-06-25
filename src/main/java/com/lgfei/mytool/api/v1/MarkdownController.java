package com.lgfei.mytool.api.v1;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.lgfei.mytool.config.SentinelRuleConfig;
import com.lgfei.mytool.converter.MarkdownConverter;
import com.lgfei.mytool.dto.GroupDocsCloudStorageDto;
import com.lgfei.mytool.exception.CommonException;
import com.lgfei.mytool.util.IOUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Api(tags = "markdown文件处理接口")
@Controller
@RequestMapping("/v1/md")
public class MarkdownController {
    private static final Logger log = LoggerFactory.getLogger(MarkdownController.class);

    private final MarkdownConverter converter;

    @Autowired
    public MarkdownController(MarkdownConverter converter){
        this.converter = converter;
    }

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
     * 4.下载生成的文件到浏览器
     * </p>
     * @return
     */
    @ApiOperation(value = "下载我的在线简历: " +
            "我的在线简历以markdown的方式保存在我的github, 通过该接口可以将.md的文件解析, 最后生成.docx文档然后下载到本地。")
    @GetMapping("/downloadResume")
    @SentinelResource(value = SentinelRuleConfig.QPS_LIMIT)
    public ResponseEntity<Resource> downloadResume(){
        String currTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //String mdFileUrl = "https://raw.githubusercontent.com/lgfei/mybook/master/resume/resume.md";
        String mdFileUrl = "https://gitee.com/lgfei/mybook/raw/master/resume/resume.md";
        String fileName = "lgf_resume_" + currTime;
        String mdFilePath = null;
        // 1.下载markdown文件
        try {
            mdFilePath = groupdocsConversionDir + fileName  + ".md";
            log.info("开始下载md文件:[{}]", mdFileUrl);
            IOUtil.downloadFile(mdFileUrl, mdFilePath);
        } catch (IOException e) {
            log.warn("md文件下载失败:[{}], 开始克隆git仓库代码", e.getMessage());
            //IOUtil.cloneRepository("https://github.com/lgfei/mybook.git", CODE_DIR, currTime);
            IOUtil.cloneRepository("https://gitee.com/lgfei/mybook.git", mybookCodeDir, currTime);
            mdFilePath = mybookCodeDir + currTime + File.separator + "resume" + File.separator + "resume.md";
        }

        // 2.将markdown文件转为html文件
        String html = null;
        try {
            String mdOrgi = IOUtil.readFileToStr(mdFilePath);
            log.info("读取md文件内容成功");
            String md = MarkdownConverter.handleSpecial(mdOrgi);
            log.info("特殊内容处理成功");
            html = MarkdownConverter.convertMarkdownToHtml(md);
            log.info("将markdown语法解析为html成功");
        } catch (Exception e) {
            throw new CommonException("将markdown文件转为html文件失败", e);
        }

        // 3.将html文件转为想要文件类型
        GroupDocsCloudStorageDto resultDto = null;
        try {
            resultDto = converter.convertHtmlToDocx(html, groupdocsConversionDir, fileName);
            log.info("html文件转化为目标文件成功");
        } catch (Exception e) {
            throw new CommonException("调用GroupDocsCloud接口失败", e);
        }

        // 4.下载生成的文件到浏览器
        try {
            InputStream inputStream = new FileInputStream(resultDto.getDownloadFile());
            log.info("开始下载目标文件");
            // 创建文件资源
            Resource resource = new InputStreamResource(inputStream);
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resultDto.getDownloadFileName() + "\"");
            // 返回文件响应
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            //e.printStackTrace();
            throw new CommonException("下载目标文件失败", e);
        }
    }
}

package com.lgfei.mytool.api.v1;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.lgfei.mytool.config.SentinelRuleConfig;
import com.lgfei.mytool.converter.MarkdownConverter;
import com.lgfei.mytool.dto.GroupDocsCloudStorageDto;
import com.lgfei.mytool.exception.CommonException;
import com.lgfei.mytool.service.MarkdownService;
import com.lgfei.mytool.util.IOUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jsoup.internal.StringUtil;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    private final MarkdownService service;

    public MarkdownController(MarkdownService service){
        this.service = service;
    }

    /**
     * 下载简历到浏览器
     * @return
     */
    @ApiOperation(value = "下载我的在线简历: " +
            "我的在线简历以markdown的方式保存在我的github, 通过该接口可以将.md的文件解析, 最后生成.pdf文档然后下载到本地。")
    @GetMapping("/downloadResume")
    @SentinelResource(value = SentinelRuleConfig.QPS_LIMIT)
    public ResponseEntity<Resource> downloadResume(){
        return this.buildDownloadResponse(service.generateResume());
    }

    @ApiOperation(value = "md内容转html")
    @PostMapping("/toHtml")
    @SentinelResource(value = SentinelRuleConfig.QPS_LIMIT)
    public ResponseEntity<Resource> toHtml(@RequestParam(value = "md") String md){
        return this.buildDownloadResponse(service.toHtml(md));
    }

    private ResponseEntity<Resource> buildDownloadResponse(GroupDocsCloudStorageDto dto){
        InputStream inputStream = null;
        try {
            // 创建文件资源
            inputStream = new FileInputStream(dto.getDownloadFile());
            Resource resource = new InputStreamResource(inputStream);
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dto.getDownloadFileName() + "\"");
            // 返回文件响应
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            //e.printStackTrace();
            throw new CommonException("下载目标文件失败", e);
        }finally {
            IOUtil.closeIOStream(inputStream, null);
        }
    }
}

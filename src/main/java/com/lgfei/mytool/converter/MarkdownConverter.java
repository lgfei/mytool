package com.lgfei.mytool.converter;

import com.groupdocs.cloud.conversion.api.ConvertApi;
import com.groupdocs.cloud.conversion.api.FileApi;
import com.groupdocs.cloud.conversion.client.ApiCallback;
import com.groupdocs.cloud.conversion.client.ApiException;
import com.groupdocs.cloud.conversion.client.Configuration;
import com.groupdocs.cloud.conversion.model.ConvertSettings;
import com.groupdocs.cloud.conversion.model.Error;
import com.groupdocs.cloud.conversion.model.FilesUploadResult;
import com.groupdocs.cloud.conversion.model.StoredConvertedResult;
import com.groupdocs.cloud.conversion.model.requests.ConvertDocumentRequest;
import com.groupdocs.cloud.conversion.model.requests.DeleteFileRequest;
import com.groupdocs.cloud.conversion.model.requests.DownloadFileRequest;
import com.groupdocs.cloud.conversion.model.requests.UploadFileRequest;
import com.lgfei.mytool.config.GroupDocsCloudConfig;
import com.lgfei.mytool.dto.GroupDocsCloudStorageDto;
import com.lgfei.mytool.exception.CommonException;
import com.lgfei.mytool.util.IOUtil;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.util.misc.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class MarkdownConverter {
    private static final Logger log = LoggerFactory.getLogger(MarkdownConverter.class);
    private final GroupDocsCloudConfig config;
    @Autowired
    public MarkdownConverter(GroupDocsCloudConfig config){
        this.config = config;
    }

    public static String handleSpecial(String markdown){
        if(null == markdown){
            return "";
        }
        int begin = markdown.indexOf("<!--hide.o-->");
        if(begin == -1){
            return markdown;
        }else{
            int end = markdown.indexOf("<!--hide.c-->");
            if(end == -1){
                return markdown;
            }
            String hideContent = markdown.substring(begin, end + 13);
            markdown = markdown.replace(hideContent, "");
            return handleSpecial(markdown);
        }
    }

    public static String convertMarkdownToHtml(String markdown) {
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, getExtensions());
        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        Parser parser = Parser.builder(options).build();
        Node document = parser.parse(markdown);

        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        String html = renderer.render(document);

        return html;
    }

    private static List<Extension> getExtensions() {
        return Arrays.asList(
                TablesExtension.create(),
                YamlFrontMatterExtension.create()
        );
    }

    public GroupDocsCloudStorageDto convertHtmlToPdf(String html, String filePath, String fileName) {
        String htmlFileName = fileName + ".html";
        String htmlFilePath = filePath + htmlFileName;
        String docxFileName = fileName + ".docx";
        FileApi fileApi = new FileApi(config.getClient().getId(), config.getClient().getSecret());
        UploadFileRequest uploadFileRequest = null;
        StoredConvertedResult convertedResult = null;

        try{
            // 上传html至 groupdocs.cloud
            IOUtil.writeStrToFile(html, htmlFilePath);
            File htmlFile = new File(htmlFilePath);
            uploadFileRequest = new UploadFileRequest(htmlFileName, htmlFile,
                    config.getStorage().getName());
            FilesUploadResult filesUploadResult =  fileApi.uploadFile(uploadFileRequest);
            List<Error> filesUploadErrors = filesUploadResult.getErrors();
            if(CollectionUtils.isEmpty(filesUploadErrors)){
                log.info("html文件上传成功");
            }else{
                filesUploadErrors.forEach(e -> {
                    log.error(e.getMessage());
                });
                throw new CommonException("html文件上传失败");
            }

            Configuration configuration = new Configuration(config.getClient().getId(), config.getClient().getSecret());
            ConvertApi convertApi = new ConvertApi(configuration);

            // 将html文件转为docx文件
            ConvertSettings toDocxSettings = new ConvertSettings();
            toDocxSettings.setFilePath(htmlFileName);
            toDocxSettings.setFormat("docx");
            toDocxSettings.setOutputPath(File.separator);
            List<StoredConvertedResult> convertedToDocxResultList = convertApi.convertDocument(new ConvertDocumentRequest(toDocxSettings));
            if(CollectionUtils.isEmpty(convertedToDocxResultList)){
                throw new CommonException("html转docx失败");
            }

            // docx文件转pdf
            ConvertSettings toPdfSettings = new ConvertSettings();
            toPdfSettings.setFilePath(docxFileName);
            toPdfSettings.setFormat("pdf");
            toPdfSettings.setOutputPath(File.separator);
            List<StoredConvertedResult> convertedToPdfResultList = convertApi.convertDocument(new ConvertDocumentRequest(toPdfSettings));
            if(CollectionUtils.isEmpty(convertedToPdfResultList)){
                throw new CommonException("docx转pdf失败");
            }

            // 下载pdf文件
            convertedResult = convertedToPdfResultList.get(0);
            DownloadFileRequest downloadFileRequest = new DownloadFileRequest(convertedResult.getPath(),
                    config.getStorage().getName(), null);
            File downloadFile = fileApi.downloadFile(downloadFileRequest);

            // 组装返回结果
            GroupDocsCloudStorageDto dto = new GroupDocsCloudStorageDto();
            dto.setDownloadFile(downloadFile);
            dto.setDownloadFileName(convertedResult.getName());
            return dto;
        }catch (ApiException e){
            throw new CommonException("调用groupdocs.cloud接口失败", e);
        }finally {
            /*// 异步清理文件
            DeleteFileRequest htmlDeleteFileRequest = new DeleteFileRequest(uploadFileRequest.getpath(), myStorageName, null);
            DeleteFileRequest docxDeleteFileRequest = new DeleteFileRequest(convertedResult.getPath(), myStorageName, null);
            List<DeleteFileRequest> deleteFileRequestList = new ArrayList<>();
            deleteFileRequestList.add(htmlDeleteFileRequest);
            deleteFileRequestList.add(docxDeleteFileRequest);
            deleteFileRequestList.forEach(deleteFileRequest -> {
                try {
                    fileApi.deleteFileAsync(deleteFileRequest, new ApiCallback<Void>() {
                        @Override
                        public void onFailure(ApiException e, int i, Map<String, List<String>> map) {
                            log.warn("文件[{}]删除失败", deleteFileRequest.getpath());
                        }

                        @Override
                        public void onSuccess(Void unused, int i, Map<String, List<String>> map) {
                            log.info("文件[{}]删除成功", deleteFileRequest.getpath());
                        }

                        @Override
                        public void onUploadProgress(long l, long l1, boolean b) {

                        }

                        @Override
                        public void onDownloadProgress(long l, long l1, boolean b) {

                        }
                    });
                } catch (ApiException e) {
                    log.warn("清理历史文件异常: {}", e.getMessage());
                }
            });*/
        }
    }
}

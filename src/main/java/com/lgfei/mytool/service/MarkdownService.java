package com.lgfei.mytool.service;

import com.lgfei.mytool.dto.GroupDocsCloudStorageDto;

import java.io.File;

/**
 *
 */
public interface MarkdownService {

    GroupDocsCloudStorageDto generateResume();
    GroupDocsCloudStorageDto toHtml(String md);
}

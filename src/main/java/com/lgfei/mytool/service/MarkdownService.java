package com.lgfei.mytool.service;

import com.lgfei.mytool.dto.GroupDocsCloudStorageDTO;

/**
 *
 */
public interface MarkdownService {

    GroupDocsCloudStorageDTO generateResume();
    GroupDocsCloudStorageDTO toHtml(String md);
}

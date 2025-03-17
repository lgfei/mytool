package com.lgfei.mytool.service;

import com.lgfei.mytool.dto.NavicatConnection;

import java.io.File;
import java.util.List;

/**
 * @author lgfei
 * @date 2025/3/17 9:24
 */
public interface NavicatService {
    List<NavicatConnection> parseNcxFile(File ncxFile, String navicatVersion);
}

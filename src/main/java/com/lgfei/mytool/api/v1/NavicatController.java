package com.lgfei.mytool.api.v1;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.lgfei.mytool.config.SentinelRuleConfig;
import com.lgfei.mytool.dto.NavicatConnection;
import com.lgfei.mytool.service.NavicatService;
import com.lgfei.mytool.util.IOUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lgfei
 * @date 2025/3/17 9:29
 */
@Api(tags = "Navicat连接处理接口")
@RestController
@RequestMapping("/v1/navicat")
public class NavicatController {

    private final NavicatService service;

    public NavicatController(NavicatService service) {
        this.service = service;
    }

    @Value("${mytool.file.storage.navicat:/opt/mytool/files/navicat/}")
    private String navicatFileStorageDir;

    @ApiOperation(value = ".ncx文件解析")
    @PostMapping("/parse_ncx_file")
    @SentinelResource(value = SentinelRuleConfig.QPS_LIMIT)
    public ResponseEntity<List<NavicatConnection>> parseNcxFile(@RequestParam("ncxFile") MultipartFile ncxFile,
                                                                @RequestParam(value = "navicatVersion", required = false, defaultValue = "11") String navicatVersion){
        return ResponseEntity.ok().body(service.parseNcxFile(IOUtil.copyFile(ncxFile, navicatFileStorageDir), navicatVersion));
    }
}

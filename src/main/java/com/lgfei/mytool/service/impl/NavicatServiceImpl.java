package com.lgfei.mytool.service.impl;

import com.lgfei.mytool.dto.NavicatConnection;
import com.lgfei.mytool.dto.NavicatConnections;
import com.lgfei.mytool.service.NavicatService;
import com.lgfei.mytool.util.Navicat11Cipher;
import com.lgfei.mytool.util.Navicat12Cipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Base64;
import java.util.List;

/**
 * @author lgfei
 * @date 2025/3/17 9:25
 */
@Service
public class NavicatServiceImpl implements NavicatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NavicatServiceImpl.class);

    @Override
    public List<NavicatConnection> parseNcxFile(File ncxFile, String navicatVersion) {
        JAXBContext context = null;
        Unmarshaller unmarshaller = null;
        NavicatConnections navicatConnections = null;
        try {
            context = JAXBContext.newInstance(NavicatConnections.class);
            unmarshaller = context.createUnmarshaller();
            navicatConnections = (NavicatConnections) unmarshaller.unmarshal(ncxFile);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        List<NavicatConnection> list = navicatConnections.getConnections();
        for (NavicatConnection navicatConnection : list) {
            String decryptPassword = navicatConnection.getPassword();
            if("11".equals(navicatVersion)){
                Navicat11Cipher cipher = new Navicat11Cipher();
                decryptPassword = cipher.DecryptString(decryptPassword);
            }else{
                Navicat12Cipher cipher = new Navicat12Cipher();
                decryptPassword = cipher.DecryptStringForNCX(decryptPassword);
            }
            navicatConnection.setPassword(decryptPassword);
        }
        return list;
    }
}

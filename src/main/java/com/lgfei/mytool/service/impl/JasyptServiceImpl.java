package com.lgfei.mytool.service.impl;

import com.lgfei.mytool.dto.JasyptDTO;
import com.lgfei.mytool.enums.JasyptAlgorithmEnum;
import com.lgfei.mytool.exception.CommonException;
import com.lgfei.mytool.service.JasyptService;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author lgfei
 * @date 2025/2/13 10:56
 */
@Service
public class JasyptServiceImpl implements JasyptService {

    private boolean check(JasyptDTO jasypt) {
        if(null == jasypt){
            throw new CommonException("jasypt is null");
        }
        if(!StringUtils.hasText(jasypt.getPassword())){
            throw new CommonException("jasypt.password is null");
        }
        if(!StringUtils.hasText(jasypt.getAlgorithm())){
            throw new CommonException("jasypt.algorithm is null");
        }
        return true;
    }

    private StandardPBEStringEncryptor buildEncryptor(JasyptDTO jasypt) {
        if(this.check(jasypt)){
            String algorithm = jasypt.getAlgorithm();
            JasyptAlgorithmEnum algorithmEnum = JasyptAlgorithmEnum.get(algorithm);
            if(null == algorithmEnum){
                throw new CommonException(String.format("not support jasypt algorithm:%s", algorithm));
            }
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(jasypt.getPassword());
            encryptor.setAlgorithm(algorithmEnum.getValue());
            return encryptor;
        }
        return null;
    }

    @Override
    public String encrypt(JasyptDTO jasypt) {
        StandardPBEStringEncryptor encryptor = this.buildEncryptor(jasypt);
        return encryptor.encrypt(jasypt.getPlainText());
    }

    @Override
    public String decrypt(JasyptDTO jasypt) {
        StandardPBEStringEncryptor encryptor = this.buildEncryptor(jasypt);
        return encryptor.decrypt(jasypt.getEncryptedText());
    }
}

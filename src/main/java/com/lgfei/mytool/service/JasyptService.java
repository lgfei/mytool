package com.lgfei.mytool.service;

import com.lgfei.mytool.dto.JasyptDTO;

/**
 * @author lgfei
 * @date 2025/2/13 10:56
 */
public interface JasyptService {

    String encrypt(JasyptDTO jasypt);

    String decrypt(JasyptDTO jasypt);
}

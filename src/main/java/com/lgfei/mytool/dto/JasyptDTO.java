package com.lgfei.mytool.dto;

import java.io.Serializable;

/**
 * @author lgfei
 * @date 2025/2/13 10:57
 */
public class JasyptDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String password;
    private String algorithm;
    private String plainText;
    private String encryptedText;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public String getEncryptedText() {
        return encryptedText;
    }

    public void setEncryptedText(String encryptedText) {
        this.encryptedText = encryptedText;
    }
}

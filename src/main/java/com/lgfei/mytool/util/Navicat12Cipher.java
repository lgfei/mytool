package com.lgfei.mytool.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;

public class Navicat12Cipher extends Navicat11Cipher {

    private SecretKeySpec _AesKey;
    private IvParameterSpec _AesIV;

    public Navicat12Cipher() {
        super();
        try {
            _AesKey = new SecretKeySpec("libcckeylibcckey".getBytes("UTF-8"), "AES");
            _AesIV = new IvParameterSpec("libcciv libcciv ".getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String EncryptStringForNCX(String plaintext) {
        try {
            Cipher AesEncryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");
            AesEncryptor.init(Cipher.ENCRYPT_MODE, _AesKey, _AesIV);
            byte[] ret = AesEncryptor.doFinal(plaintext.getBytes("UTF-8"));
            return DatatypeConverter.printHexBinary(ret);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String DecryptStringForNCX(String ciphertext) {
        try {
            Cipher AesEncryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");
            AesEncryptor.init(Cipher.DECRYPT_MODE, _AesKey, _AesIV);
            byte[] ret = AesEncryptor.doFinal(DatatypeConverter.parseHexBinary(ciphertext));
            return new String(ret, Charset.forName("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

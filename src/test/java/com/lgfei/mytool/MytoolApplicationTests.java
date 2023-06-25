package com.lgfei.mytool;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MytoolApplicationTests {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    void contextLoads() {
    }

    /**
     * 加密解密测试
     */
    @Test
    public void jasyptTest() {
        // 加密
        System.out.println(stringEncryptor.encrypt("我是明文"));
        // 解密
        System.out.println(stringEncryptor.decrypt("我是密文"));
    }

    /**
     * 加密解密手动测试
     */
    @Test
    public void jasyptManualTest() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("jaspyt_password");
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        System.out.println(encryptor.encrypt("我是明文"));
        System.out.println(encryptor.decrypt("我是密文"));
    }
}

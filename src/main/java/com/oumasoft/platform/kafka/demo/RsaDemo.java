package com.oumasoft.platform.kafka.demo;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.oumasoft.platform.kafka.tool.utils.RsaUtil;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author crystal
 */
public class RsaDemo {

    public static void main(String[] args) throws Exception{
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgUSWfaRUXEqQaU4Gh0IPxaCkXGiz6QqLaC9E5g/lauFg0bY35gMGqSfFkWdEzyxjxrvDnJB/dCFuuoLEEHwknfZLMAawnCg8FylsPr6hOTJYL2ghY4zjJdU8Kayab58wM2h1PnZC+BStA7BvOEPcmv/tn5GVTIscxXidrpSdu0QIDAQAB";
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKBRJZ9pFRcSpBpTgaHQg/FoKRcaLPpCotoL0TmD+Vq4WDRtjfmAwapJ8WRZ0TPLGPGu8OckH90IW66gsQQfCSd9kswBrCcKDwXKWw+vqE5MlgvaCFjjOMl1TwprJpvnzAzaHU+dkL4FK0DsG84Q9ya/+2fkZVMixzFeJ2ulJ27RAgMBAAECgYAhSRfdpCy70KP/z0y/8xIiAjVd1ZJ3F3TGfuOe5HbWTiYKJKDwp7oOCDI1Wqyj60wvidn8s7Rl6QFYHYcDSwpUM31EyYQrG4tzLrlsSaIDHKHAUT3BI7qEEPD5aZ90FFFsm/1fThmD8Ow4I0Lb4MoFYZavCyJ5KRIxbJ2PumSF/QJBANMWL1K6/HLBr8RppDly4UKsbBs7LSo1VirxN0d+/QBOLOe4Q7SLVweULIN39FB2OME3+eVmr9tDbeNCLNI2KV8CQQDCbY5GqGH3L48QQq0yP1h7XNdIr9BkJpRk7k719V15WJF8Pn3M5dT31IF7fdgQuk8GiERdTHvkSKj0xZgT5uXPAkEAoEikPonFz83LDbW6AfV9nwcWP8c7kGVXvNvVQfDnZCf4aSu+HYYA27xj4KBhYAelKPK8Srkip7gX5Q0Gw8hkuQJBAKAWqRGQz6lCZZ/T5tv6wJZC6ZiV+xablZBNkIKA18h0nsYYhmjRzTuKpFrUUbXkUfFk/jV8Jn7fPQ7eK8wnUI0CQHf+xZlGTLPbmVuuNANJ1xaTh7cTzfTzFbPN7xpIsSyqhTZWslv1J4nxuKIiK4CJBPWvvhoOBSztCsOLpS4OSjA=";
        String headerValue = "anonymous:anonymous_secret";
        String base64ValueOnline = "YW5vbnltb3VzOmFub255bW91c19zZWNyZXQ=";
        String base64ValueLocal = new String(Base64.getEncoder().encode(headerValue.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);;
        if (base64ValueOnline.equals(base64ValueLocal)) {
            System.out.println("本地线上base64加密一致");
        }
        String token = new String(Base64.getDecoder().decode(base64ValueLocal.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        if (token.equals(headerValue)) {
            System.out.println("base64加解密一致----");
            System.out.println("原始值:"+headerValue);
            System.out.println("本地解密值:"+token);
            System.out.println("加密值:"+base64ValueLocal);
        }

        String basicHeaderValue = "Basic "+ base64ValueLocal;
        String rsaPublicEncrypt = RsaUtil.encryptByPublicKey(publicKey, basicHeaderValue);
        String rsaDecryptValue = RsaUtil.decryptByPrivateKey(privateKey, rsaPublicEncrypt);

        if (basicHeaderValue.equals(rsaDecryptValue)) {
            System.out.println("RSA加解密一致---");
            System.out.println("原始值:"+basicHeaderValue);
            System.out.println("解密值:"+rsaDecryptValue);
            System.out.println("加密值:"+rsaPublicEncrypt);
        }

        RSA rsaGenerate = new RSA();

        //获得公钥
        rsaGenerate.getPublicKey();
        //String hutoolPublicKey = rsaGenerate.getPublicKeyBase64();
        String hutoolPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCRCD2X4lP6nVTpxvmb3Sy8JyAQWHgD48ln4heWlgPZmc+/pZx7V4nGR+L55/j0wTHfSxGqlx/lrsepEL97VyG+4xDd5GQBGRz/xEM5j7AjdyKcHPAV545gn+9h0YZx6YqrR/nWlmXtKcHQWNyUu9LEmt5HTrC5dmYa3atHFB0+bwIDAQAB";
        System.out.println("hutool获得公钥:\n"+hutoolPublicKey);

        //获得私钥
        rsaGenerate.getPrivateKey();
        //String hutoolPrivateKey = rsaGenerate.getPrivateKeyBase64();
        String hutoolPrivateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJEIPZfiU/qdVOnG+ZvdLLwnIBBYeAPjyWfiF5aWA9mZz7+lnHtXicZH4vnn+PTBMd9LEaqXH+Wux6kQv3tXIb7jEN3kZAEZHP/EQzmPsCN3Ipwc8BXnjmCf72HRhnHpiqtH+daWZe0pwdBY3JS70sSa3kdOsLl2Zhrdq0cUHT5vAgMBAAECgYAw17yehQtRMGEjZe40DpV9PBPxTUoFzVXL1XmqLhVX2BP979E2PLHag+Ut0nCmRNX95DMr5z9Hw8fb6RcDe8GSHNLY7IufXhTWJMy38oEVBqu9fWb/cEr0uRFSr5v9fnmqL89lM6v4MH9IhEtNeNLJW5IOGg78bpq1/2YUdKRVmQJBAMjXidQaP1N8u62s60XnYCRtVxe2+KtXVGKZdHAs0QZ2dV5OSo7S737XuzsXFYqt5bzboB4lyWvqfzovPGoG1NMCQQC43Owwzh/7X4Sj99UigolygOMQ1u5cTgB9VoiESjNgSZ2xypLxF+2HMh+Pa33qJNtcNqqfIDvAfdnmgJQadd51AkB0e93qcFauGzfWoOXLo0g1mFzc2jgbWAkPLqVDshqBPw8PZSUE9P4GeSSUPu01g2otQMv68DuEJXsoyeCmNdQVAkBiWof/X2t8nlE/u7fV6IWgOhqU9wVSbt2r0cJIQAIdlI0l7pVHWGezqmNINvSDAqEC6sLUbp3byBmKnZYujc+RAkAFNMUTcr1FJHnkDJ+fuH9GER99evbYuicGuiwXnyIn2exyZkczn7zMccmVGNFdTzuqdaUX1Xc6BBAjE8ihIabA";
        System.out.println("hutool获得私钥:\n"+hutoolPrivateKey);

        //String testStr = "测试test123";
        String testStr = basicHeaderValue;
        String rsaEncryptStr = RsaUtil.encryptByPublicKey(rsaGenerate.getPublicKeyBase64(), testStr);
        System.out.println("RSAUtil使用hutool公钥加密:"+rsaEncryptStr);
        String rsaDecryptStr = RsaUtil.decryptByPrivateKey(rsaGenerate.getPrivateKeyBase64(), rsaEncryptStr);
        System.out.println("RSAUtil使用hutool私钥解密:"+rsaDecryptStr);
        //RSA rsa = new RSA(privateKey,publicKey);
        RSA rsa = new RSA(hutoolPrivateKey,hutoolPublicKey);
        byte[] encrypt = rsa.encrypt(testStr, KeyType.PublicKey);
       // String encryptData = new String(encrypt,StandardCharsets.UTF_8);
        String encryptData1 = rsa.encryptHex(testStr, KeyType.PublicKey);
        String encryptData2 = rsa.encryptBase64(testStr, KeyType.PublicKey);
        System.out.println("hutool公钥加密1："+encryptData1);
        System.out.println("hutool公钥加密2："+encryptData2);

        String decryptData1 = rsa.decryptStr(encryptData1, KeyType.PrivateKey);
        String decryptData2 = rsa.decryptStr(encryptData2, KeyType.PrivateKey);

        System.out.println("hutool私钥解密1："+decryptData1);
        System.out.println("hutool私钥解密2："+decryptData2);

        RSA rsaPublic = new RSA(null,hutoolPublicKey);
        String from1 = rsaPublic.encryptHex(testStr, KeyType.PublicKey);
        String from2 = rsaPublic.encryptBase64(testStr, KeyType.PublicKey);
        System.out.println("业务测试加密1："+from1);
        System.out.println("业务测试加密1长度："+from1.length());
        System.out.println("业务测试加密2："+from2);
        System.out.println("业务测试加密2长度："+from2.length());
        RSA rsaPrivate = new RSA(hutoolPrivateKey,null);
        String to = rsaPrivate.decryptStr(from1, KeyType.PrivateKey);
        System.out.println("业务测试解密："+to);

    }

}

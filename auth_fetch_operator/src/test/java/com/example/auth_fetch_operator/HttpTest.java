package com.example.auth_fetch_operator;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/10 22:31
 */
public class HttpTest {


    @Test
    public void proxyTest() throws Exception{
        Connection.Response authorization = Jsoup.connect("http://www.baidu.com").proxy("47.106.232.62",10086)
                .ignoreHttpErrors(true).timeout(1024*1024).ignoreContentType(true).execute();
        System.err.println(authorization.statusCode());
        System.out.println(authorization.body());
    }
    static Charset CHARSET = Charset.forName("UTF-8");


    @Test
    void base64() throws Exception{
        byte[] decode = java.util.Base64.getDecoder().decode("Prm9PT06Pz86Oz8ODeNX4nIGDQ0N");
        byte[] decrypt = decrypt(decode);
        System.out.println(new String(decrypt, CHARSET));
    }

    public byte[] decrypt(byte[] compressed) {
        ByteArrayOutputStream decompressed = new ByteArrayOutputStream();
        DecryptedInputStream decrypt = null;
        try {
            decrypt = new DecryptedInputStream(compressed);
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = decrypt.read(buffer)) != -1) {
                decompressed.write(buffer, 0, offset);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (decrypt != null) {
                try {
                    decrypt.close();
                } catch (IOException e) {
                }
            }

        }

        return decompressed.toByteArray();
    }
}

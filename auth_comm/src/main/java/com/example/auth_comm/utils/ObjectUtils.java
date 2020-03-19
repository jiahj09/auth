package com.example.auth_comm.utils;

import java.io.*;
import java.util.Base64;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/18 20:25
 */
public class ObjectUtils {

    /**
     * java对象序列化成字节数组
     *
     * @param object
     * @return
     */
    public static byte[] toBytes(Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * java 对象，序列化为base64字符串
     *
     * @param object
     * @return
     */
    public static String toBase64(Object object) {
        byte[] bytes = toBytes(object);
        return Base64.getEncoder().encodeToString(bytes);
    }


    /**
     * 字节数组反序列化成java对象
     *
     * @param bytes
     * @return
     */
    public static Object toObject(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
            Object object = ois.readObject();
            return object;
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 被编码成base64字符串对象，转换成对象
     *
     * @param base64
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T base642Obj(String base64, Class<T> clazz) {
        byte[] decode = Base64.getDecoder().decode(base64);
        Object o = toObject(decode);
        return (T) o;
    }
}

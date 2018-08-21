package com.roli.common.utils.security;

import com.roli.common.exception.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author xuxinyu
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: 提供AES方式的加解密方法
 * @date 2018/1/26 下午8:45
 */
public class AESHandle {

    private static final Logger logger = LoggerFactory.getLogger(AESHandle.class);

    private static final String secretModel = "AES";

    //若外部不传入密钥，则使用此密钥
    private static final String DEFAULT_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC3AfmzaxeUGSi+nMzV7LI960TeevTox2iZGrxw/B";

    /**
     * AES加密，并返回加密后的密文
    * @Description:
    * @param secretKey 加密密钥
     *@param context 需要加密的内容
    * @return String
    * @throws Exception 需要外部处理
    * @author xuxinyu
    * @date 2018/1/27 16:15
    */
    public static String encryptAES(String secretKey,String context) throws SecurityException{

        try{
            if(context==null){
                logger.error("加密内容不可为空！");
                return null;
            }
            if(secretKey == null){
                secretKey = DEFAULT_KEY;
            }

            //需要加密的内容要指定UTF-8编码，用于汉字类编码
            byte[] contextByte = context.getBytes("utf-8");
            byte[] resultStrByte = initCipher(secretKey,true).doFinal(contextByte);

            String resultStr = byte2StringWithBase64(resultStrByte);

            return resultStr;
        }catch (NoSuchPaddingException e) {

            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");

        } catch (InvalidKeyException e) {

            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");

        } catch (IllegalBlockSizeException e) {

            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");

        } catch (BadPaddingException e) {

            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        }
    }


    /**
     *  AES解密，并返回解密后的原文
    * @Description:
    * @param secretKey
     * @param contextSecret
    * @return String
    * @throws
    * @author xuxinyu
    * @date 2018/1/27 23:21
    */
    public static String decryptAES(String secretKey,String contextSecret)
            throws SecurityException{

        try{
            if(contextSecret==null){
                logger.error("密文内容不可为空！");
                return null;
            }
            if(secretKey == null){
                secretKey = DEFAULT_KEY;
            }

            byte[] contextSecretByte = string2ByteWithBase64(contextSecret);
            byte[] resultStrByte = initCipher(secretKey,false).doFinal(contextSecretByte);

            String resultStr  = new String(resultStrByte,"utf-8");

            return resultStr;
        }catch (NoSuchPaddingException e){
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (BadPaddingException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        }
    }




    /**
     * 对秘钥进行初始化处理，并返回一个Cipher对象
    * @Description:
    * @param secretKey
    * @return Cipher
    * @throws
    * @author xuxinyu
    * @date 2018/1/26 下午9:08
    */
    private static Cipher initCipher(String secretKey,boolean encrypt)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

        /*
        * 初始化一个Cipher对象并返回
        * 如果传入为true，则进行加密操作
        * 否则进行解密操作
        * */

        KeyGenerator keyGenerator = KeyGenerator.getInstance(secretModel);

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(secretKey.getBytes());

        keyGenerator.init(128,random);
        SecretKey initSecretKey = keyGenerator.generateKey();
        byte[] initSecretKeyByte = initSecretKey.getEncoded();
        SecretKey realSecretKey = new SecretKeySpec(initSecretKeyByte,secretModel);

        Cipher cipher = Cipher.getInstance(secretModel);
        if(encrypt){
            cipher.init(Cipher.ENCRYPT_MODE,realSecretKey);
        }else{
            cipher.init(Cipher.DECRYPT_MODE,realSecretKey);
        }
        return cipher;

    }

    private static String byte2StringWithBase64(byte[] byteData){
        //return new BASE64Encoder().encode(byteData);
        return Base64.getEncoder().encodeToString(byteData);

    }

    private static byte[] string2ByteWithBase64(String strData){
        //return new BASE64Decoder().decodeBuffer(strData);
        return Base64.getDecoder().decode(strData);

    }


    public static void main(String[] args) throws Exception{

       // String secretKey = "请输入加密的规则";
        String context = "";

        System.out.println(encryptAES(null,context));

        System.out.println(decryptAES(null,encryptAES(null,context)));
    }


}

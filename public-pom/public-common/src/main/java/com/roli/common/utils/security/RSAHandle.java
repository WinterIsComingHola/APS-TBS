package com.roli.common.utils.security;

import com.roli.common.exception.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuxinyu
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: 提供RSA方式的加减密方法
 * @date 2018/1/27 23:27
 */
public class RSAHandle {

    private static final Logger logger = LoggerFactory.getLogger(RSAHandle.class);
    /**
     * 加密算法RSA
     */
    private static final String secretModel = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    private static final String RSA_PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String RSA_PRIVATE_KEY = "RSAPrivateKey";


    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;



    /**
     * 使用公钥进行加密
    * @Description:
    * @param context 需要加密的内容
     * @param publicKey 公钥串
    * @return String 加密完成的密文，经过了Base64编码
    * @throws SecurityException 受检查的异常类，外部需要处理
    * @author xuxinyu
    * @date 2018/1/29 0:15
    */
    public static String encrptByPublicKey(String context,String publicKey)
            throws SecurityException{


        try{
            byte[] contextByte = context.getBytes("UTF-8");
            byte[] publicKeyByte = string2ByteWithBase64(publicKey);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyByte);
            KeyFactory keyFactory = KeyFactory.getInstance(secretModel);
            Key pubKey = keyFactory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            //Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");

            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            byte[] encryptedData = offSetHandle(contextByte,cipher);
            return byte2StringWithBase64(encryptedData);
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        }
            catch (NoSuchPaddingException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        }

    }


    /**
     * 使用私钥对密文进行解码
    * @Description:
    * @param sercetContext 需要解密的密文，必须经过Base64编码
     * @param privateKey 私钥串
    * @return String 原始的明文
    * @throws SecurityException 受检查的异常类，外部需要处理
    * @author xuxinyu
    * @date 2018/1/29 0:46
    */
    public static String decrptByPrivateKey(String sercetContext,String privateKey)
            throws SecurityException {


        try{
            byte[] sercetContextByte = string2ByteWithBase64(sercetContext);
            byte[] privateKeyByte = string2ByteWithBase64(privateKey);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyByte);
            KeyFactory keyFactory = KeyFactory.getInstance(secretModel);
            Key priKey = keyFactory.generatePrivate(keySpec);


            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            //Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, priKey);

            byte[] decryptedData = offSetHandle(sercetContextByte,cipher);
            return new String(decryptedData,"UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (SecurityException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        }
    }


    /**
     * 使用私钥进行签名，并返回签名结果
     * @param secretContextBytes 需要签名的内容，必须使用私钥加密后的二进制数组
     *@param privateKey 私钥串
     * @return String  签名结果
     * @throws SecurityException 受检查的异常类，外部需要处理
     * @author xuxinyu
     * @date 2018/1/30 23:57
     */
    public static String signByPrivateKey(byte[] secretContextBytes,String privateKey)
            throws SecurityException {

        try{
            //byte[] contextBytes = context.getBytes("UTF-8");
            byte[] privateKeyBytes = string2ByteWithBase64(privateKey);

            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(secretModel);
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateK);
            signature.update(secretContextBytes);

            return byte2StringWithBase64(signature.sign());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (SignatureException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        }

    }



    /**
     * 使用公钥对私钥的签名结果进行校验。并返回布尔值
    * @param secretContextBytes 需要签名的内容
     *@param publicKey 公钥串
     *@param sign 私钥签名后的结果
    * @return boolean 校验TURE 或者FALSE
    * @throws SecurityException 受检查的异常类，外部需要处理
    * @author xuxinyu
    * @date 2018/1/31 0:16
    */
    public static boolean verifySignByPublicKey(byte[] secretContextBytes, String publicKey, String sign) throws SecurityException {

        try{
            byte[] keyBytes = string2ByteWithBase64(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(secretModel);
            PublicKey publicK = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicK);
            signature.update(secretContextBytes);
            return signature.verify(string2ByteWithBase64(sign));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (SignatureException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        }

    }


    /**
     * 使用私钥进行加密，加密的结果（二进制数组）一般用于私钥的签名
    * @param contextBytes 需要加密的内容，请使用String的getbytes()方法获取二进制数组
     *@param privateKey 私钥串
    * @return byte[] 加密后的结果
    * @throws SecurityException 受检查的异常类，外部需要处理
    * @author xuxinyu
    * @date 2018/1/31 0:19
    */
    public static byte[] encryptByPrivateKey(byte[] contextBytes, String privateKey)
            throws SecurityException {

        try{
            byte[] keyBytes = string2ByteWithBase64(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(secretModel);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateK);

            return offSetHandle(contextBytes,cipher);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        }
    }



    /**
     * 使用公钥进行解密，一般情况下不需要用这种场景
    * @param secretContextBytes 私钥加密后的二进制数组
     *@param publicKey 公钥串
    * @return byte[] 解密后的二进制数组，如果想转换为字符串，请使用new String进行操作
    * @throws SecurityException  受检查的异常类，外部需要处理
    * @author xuxinyu
    * @date 2018/1/31 0:21
    */
    public static byte[] decryptByPublicKey(byte[] secretContextBytes, String publicKey)
            throws SecurityException {

        try{
            byte[] keyBytes = string2ByteWithBase64(publicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(secretModel);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicK);

            return offSetHandle(secretContextBytes,cipher);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        }
    }


    /**
     * 对数据分段加密
    * @Description:
    * @param
    * @return ${return_type}
    * @throws SecurityException
    * @author xuxinyu
    * @date 2018/2/1 下午5:48
    */
    private static byte[] offSetHandle(byte[] secretContextBytes,Cipher cipher) throws SecurityException {

        try{
            int inputLen = secretContextBytes.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(secretContextBytes, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(secretContextBytes, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        }catch (BadPaddingException e){
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        }catch (IllegalBlockSizeException e){
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        }catch (IOException e){
            e.printStackTrace();
            logger.error("处理加密解密操作异常！！");
            throw new SecurityException("处理加密解密操作异常！！");
        }


    }



    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return String 私钥串
     * @throws Exception 受检查的异常类，外部需要处理
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(RSA_PRIVATE_KEY);
        return byte2StringWithBase64(key.getEncoded());
    }

    /**
     * <p>
     * 获取公钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return String 公钥串
     * @throws Exception 受检查的异常类，外部需要处理
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(RSA_PUBLIC_KEY);
        return byte2StringWithBase64(key.getEncoded());
    }

    
    /**
     * 获得公钥和私钥并存在Map中
    * @Description:
    * @param
    * @return Map<String,Object>
    * @throws NoSuchAlgorithmException NoSuchAlgorithmException异常，外部需要处理
    * @author xuxinyu
    * @date 2018/1/28 23:25
    */
    public static Map<String,Object> getKey() throws NoSuchAlgorithmException {
        Map<String,Object> keyMap = new HashMap<>();

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(secretModel);
        keyPairGen.initialize(1024);//1024代表密钥二进制位数
        KeyPair keyPair = keyPairGen.generateKeyPair();

        //获得公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //获得私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        keyMap.put(RSA_PUBLIC_KEY,publicKey);
        keyMap.put(RSA_PRIVATE_KEY,privateKey);
        return keyMap;
    }


    /**
     * 提供Base64方式的编码操作，将byte[]数组转换为String
    * @Description:
    * @param byteData 需要被加密的byte[]
    * @return String 加密后的密文
    * @throws
    * @author xuxinyu
    * @date 2018/1/28 23:11
    */
    private static String byte2StringWithBase64(byte[] byteData){
        //return new BASE64Encoder().encode(byteData);
        return Base64.getEncoder().encodeToString(byteData);

    }


    /**
     * 提供Base64方式的解码操作，将String转换为byte[]
    * @Description:
    * @param strData 需要解码的字符串
    * @return byte[] 解码完成后的byte数组
    * @throws
    * @author xuxinyu
    * @date 2018/1/28 23:11
    */
    private static byte[] string2ByteWithBase64(String strData){
        //return new BASE64Decoder().decodeBuffer(strData);
        return Base64.getDecoder().decode(strData);

    }

    public static void main(String[] agrs) throws Exception {


        Map<String, Object> keyMap = getKey();

        System.err.println("公钥加密——私钥解密");
        String source = "徐新宇你好！";
        System.out.println("\r加密前文字：\r\n" + source);

        System.out.println("公钥加密后文字："+encrptByPublicKey(source,getPublicKey(keyMap)));

        System.out.println("私钥解密后文字："+decrptByPrivateKey(encrptByPublicKey(source,getPublicKey(keyMap)),getPrivateKey(keyMap)));



        /*测试私钥加密公钥解密  以及签名操作*/
        System.err.println("私钥加密——公钥解密");
        String source2 = "徐新宇测试！！";
        System.out.println("\r加密前文字：\r\n" + source2);

        System.out.println("私钥加密后文字："+byte2StringWithBase64(encryptByPrivateKey(source2.getBytes("utf-8"),getPrivateKey(keyMap))));

        byte[] decryptByPub = decryptByPublicKey(encryptByPrivateKey(source2.getBytes("utf-8"),getPrivateKey(keyMap)),getPublicKey(keyMap));

        System.out.println("公钥解密后文字："+new String(decryptByPub,"utf-8"));

        /*
        *私钥签名，公钥验证
        *
        * 签名的一般用法：
        *
        * 本系统将需要签名的内容放入map中，使用私钥进行签名获得sign，将签名后的值也塞入这个map中，
        * 外源系统获得这个map，获取到sign中单独存放，再将除了sign后的内容和sign以及公钥一起进行校验
        *
        * */
        System.err.println("私钥签名——公钥验证");

        String signContext = "sign内容";
        System.out.println("\r要签名的内容：\r\n" + signContext);
        byte[] signContextBytes = signContext.getBytes("utf-8");

        String signResult = signByPrivateKey(signContextBytes,getPrivateKey(keyMap));

        System.out.println("\r签名后的内容：\r\n" + signResult);

        System.out.println("\r公钥校验签名：\r\n" + verifySignByPublicKey(signContextBytes,getPublicKey(keyMap),signResult));


    }

}

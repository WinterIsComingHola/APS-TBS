package com.roli.common.utils.security;

import com.roli.common.exception.SecurityException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 *
 * 使用HmacHa1方式对字符串进行签名校验，包括加密和校验两个方法
 *
 * @author xuxinyu
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description:
 * @date 2018/2/7 上午10:25
 */
public class HmacHa1Handle {

    private static final Logger logger = LoggerFactory.getLogger(RSAHandle.class);

    private static final String SECRETMODEL = "HmacSHA1";

    /**
    * 使用HMACSHA1对字符串进行加密操作，并返回加密的密文
     *
    * @param context 需要被加密的内容
     * @param key 双方约定的密钥
    * @return String 生成的密文
    * @throws SecurityException 统一外部处理
    * @author xuxinyu
    * @date 2018/2/7 上午10:29
    */
    public static String signWithHmacSha1(String context,String key) throws SecurityException {

        try{

            if(context == null){
                throw new SecurityException("加密内容不可为空！！");
            }

            byte[] contextBytes = context.getBytes("UTF-8");
            byte[] keyBytes = key.getBytes("UTF-8");

            Mac mac = Mac.getInstance(SECRETMODEL);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes,SECRETMODEL);
            mac.init(secretKeySpec);
            byte[] resultDate = mac.doFinal(contextBytes);
            return byte2StringWithBase64(resultDate);

        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), ExceptionUtils.getStackTrace(e));
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), ExceptionUtils.getStackTrace(e));
            throw new SecurityException("处理加密解密操作异常！！");
        } catch (InvalidKeyException e) {
            logger.error(e.getMessage(), ExceptionUtils.getStackTrace(e));
            throw new SecurityException("处理加密解密操作异常！！");
        }

    }

    /**
    * 对内容进行校验
    * @param context 外部传入的需要明文的内容
     * @param signContext 外部传入的密文的内容
     * @param key 双方共同的密钥
    * @return boolean 校验结果
    * @throws
    * @author xuxinyu
    * @date 2018/2/7 上午10:48
    */
    public static boolean verify(String context,String signContext,String key) throws SecurityException {

        if(context == null || signContext == null){
            return false;
        }
        String handlecontext  = signWithHmacSha1(context,key);

        return handlecontext.equals(signContext);
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
        if(byteData.length == 0){
            return null;
        }
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

    public static void main(String[] args) throws SecurityException {

        String context = "192.168.host.name";
        String key = "xuxinyu";
        String encryptcontext = signWithHmacSha1(context,key);

        System.out.println("加密后的密文："+encryptcontext);

        boolean isok = verify(context,encryptcontext,key);
        System.out.println("验证结果："+isok);

    }

}

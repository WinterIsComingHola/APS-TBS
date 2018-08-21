package com.roli.common.utils.serialize;

import com.esotericsoftware.kryo.io.Input;

import java.util.StringTokenizer;

/**
 * 
 * Class Name : DeserializeUtils<br>
 * 
 * Description : <br>
 * 
 * @author xuxinyu
 * @date 2017年8月13日
 * @date 上午9:32:41
 * @see
 *
 */
public class DeserializeUtils {

    public static <T> T deserialize(byte[] bytes, Class<T> classz) {
        Input input = new Input(bytes);
        T object = SerializeUtils.kryo.readObject(input, classz);
        input.close();
        return object;
    }

    public static <T> T deserialize(String str, Class<T> classz) {
        StringTokenizer token = new StringTokenizer(str, SerializeUtils.SEPARATOR);
        byte[] bytes = new byte[token.countTokens()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = Byte.valueOf(token.nextToken());
        }
        return deserialize(bytes, classz);
    }

}

package com.roli.common.utils.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import java.util.StringTokenizer;

/**
 * 
 * Class Name : SerializeUtils<br>
 * 
 * Description : 序列化类kryo方案<br>
 * 为了性能,不要对大对象进行序列化操作<br>
 * 
 * @author xuxinyu
 * @date 2017年8月13日
 * @date 上午9:31:14
 * @see
 *
 */
public class SerializeUtils {
    public final static Kryo kryo = new Kryo();
    public final static int DEFAULT_MAXBUFSIZE = 1024 * 10;
    public final static String SEPARATOR = ",";

    public static byte[] serialize(Object object) {
        return serialize(object, DEFAULT_MAXBUFSIZE);
    }

    public static byte[] serialize(Object object, int maxBufSize) {
        Output output = new Output();
        output.setBuffer(new byte[1024], maxBufSize);
        kryo.writeObject(output, object);
        byte[] bytes = output.toBytes();
        output.close();
        return bytes;
    }

    public static String serialize2str(Object object) {
        return serialize2str(object, DEFAULT_MAXBUFSIZE);
    }

    public static String serialize2str(Object object, int maxBufSize) {
        byte[] bytes = serialize(object, maxBufSize);
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(b + SEPARATOR);
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public static void main(String[] args) {
        if (true) {
            String sss = "1,2,3,4,5,6,7,8,10";
            byte[] bytes = sss.getBytes();
        }

        StringBuilder builder = new StringBuilder();
        long s1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            builder.append(123 + SEPARATOR);
        }
        System.out.println(System.currentTimeMillis() - s1);
        String string = builder.toString();
        long s = System.currentTimeMillis();
        string.split(SEPARATOR);
        System.out.println(System.currentTimeMillis() - s);

        s = System.currentTimeMillis();
        StringTokenizer token = new StringTokenizer(string, ",");
        while (token.hasMoreElements()) {
            Byte.valueOf(token.nextToken());
        }
        System.out.println(System.currentTimeMillis() - s);

        s = System.currentTimeMillis();
        StringBuilder builder2 = new StringBuilder();
        CharSequence subSequence = null;
        for (Character c : builder.toString().toCharArray()) {
            subSequence = builder.substring(0, 4);
        }
        System.out.println(System.currentTimeMillis() - s);
    }
}

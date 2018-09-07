package com.roli.common.utils.base;

public class charUtil {

    public static String lowerFirst(String oldStr){

        char[]chars = oldStr.toCharArray();

        chars[0] += 32;

        return String.valueOf(chars);

    }

    public static String upperFirst(String oldStr){

        char[]chars = oldStr.toCharArray();

        chars[0] -= 32;

        return String.valueOf(chars);

    }

    public static void main(String[] args){
        System.out.println(upperFirst("CoolGirl"));
    }

}

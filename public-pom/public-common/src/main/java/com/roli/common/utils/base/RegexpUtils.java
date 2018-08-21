package com.roli.common.utils.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexpUtils {


    public static boolean find(String patternStr,String compareStr){

        //String patternStr = "^\\d{5}";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(compareStr);

        if(matcher.find()){
            return true;
        }else{
            return false;
        }
    }

    public static String getSubStr(String patternStr,String sourceStr){

        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(sourceStr);

        if(matcher.find()){
            return matcher.group();
        }
        return null;

    }

}

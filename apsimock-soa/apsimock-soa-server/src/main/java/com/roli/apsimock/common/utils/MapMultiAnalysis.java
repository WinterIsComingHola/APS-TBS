package com.roli.apsimock.common.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author xuxinyu
 * @date 2018/4/9 下午3:07
 */
public class MapMultiAnalysis {



     /**
     * 使用递归对Map下的子元素进行遍历
      * 如果map中的key和传入的father相等，则将childkey和value写入这个farherkey中
     * @param handleMap
      * @param fatherKey
      * @param childKey
      * @param childObj
     * @return Map<String,Object>
     * @author xuxinyu
     * @date 2018/7/23 下午5:23
     */
    public static Map<String,Object> convertMap(Map<String,Object> handleMap
                                                ,String fatherKey
                                                ,String childKey
                                                ,Object childObj
                                                ,boolean isUnderList){

        if(handleMap.size()==0){
            //如果handleMap没有任何值，则当前的子map可以直接加入到handleMap中
            handleMap.put(childKey,childObj);
        }else{
            for(Map.Entry<String,Object> mapEntry: handleMap.entrySet()){
                if(mapEntry.getValue() instanceof Map){
                    if(mapEntry.getKey().equals(fatherKey)){
                        Map<String,Object> targetMap = (Map<String,Object>)mapEntry.getValue();
                        targetMap.put(childKey,childObj);
                    }else{
                        //使用递归对内部的Map进行进一步处理
                        convertMap((Map<String,Object>)mapEntry.getValue(),fatherKey,childKey,childObj,false);
                    }
                }else if(mapEntry.getValue() instanceof List){
                    //这里是认为：list内部一定为map才能够继续承载子集，不支持多层list处理
                    for(Map<String,Object> lmap:(List<Map<String,Object>>)mapEntry.getValue()){
                        convertMap(lmap,fatherKey,childKey,childObj,true);
                    }
                }else{
                    //如果不属于上述两种复合类型，则当前的子map可以直接加入到handleMap中
                    if(isUnderList){
                        handleMap.put(childKey,childObj);
                        break;
                    }
                }
            }
        }

        return handleMap;
    }

    /**
     * 使用递归对List下的子元素进行遍历，并将和fatherkey相同的childObject加入List
     * 在当前的策略中，如果List下面为普通数据类型则直接add进去
     * 如果不是普通数据类型，则默认为List下面的元素必须是一个Map，这样才可以用来承载后续的子List，不支持二维List数据结构
     * @param handleMap
     * @param fatherKey
     * @param childObj
     * @return Map<String,Object>
     * @author xuxinyu
     * @date 2018/7/23 下午5:23
     */
    public static Map<String,Object> convertList(Map<String,Object> handleMap
            ,String fatherKey
            ,Object childObj){
        for(Map.Entry<String,Object> mapEntry: handleMap.entrySet()){
            if(mapEntry.getValue() instanceof List){
                if(mapEntry.getKey().equals(fatherKey)){
                    List<Object> targetList = (List<Object>)mapEntry.getValue();
                    targetList.add(childObj);
                }else{
                    //这里是认为：list内部一定为map才能够继续承载子集，不支持多层list处理
                    for(Map<String,Object> lmap:(List<Map<String,Object>>)mapEntry.getValue()){
                        convertList(lmap,fatherKey,childObj);
                    }
                }
            }else if(mapEntry.getValue() instanceof Map){
                Map<String,Object> targetMap = (Map<String,Object>)mapEntry.getValue();
                convertList(targetMap,fatherKey,childObj);
            }
        }
        return handleMap;

    }


    /**
     * 对传入的Map进行key值处理，为了保证全局唯一，外部传入的map有些key是数字+字符串的形式
     * 这个方法将数字去掉，仅仅保留字符串
     * @param handleMap
     * @return Map<String,Object>
     * @author xuxinyu
     * @date 2018/7/23 下午5:23
     */
    public static Map<String,Object> handleMapWithNumKey(Map<String,Object> handleMap,Map<String,Object> targetMap){
        for(Map.Entry<String,Object> mapEntry: handleMap.entrySet()){

            //定义搜索map中的目标key的正则，如果key的前五个字符为数字，则匹配之
            String patternStr = "^\\d{5}";
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(mapEntry.getKey());

            //匹配成功
            if(matcher.find()){
                /*替换策略
                * 将目标key的非数字字符作为替换后的字符串并提取出来，
                * 提取策略为1个或者多个非数字的字符串，要求必须为尾部字符
                * ?表示前面的表达式可有可没有
                * */
                String patternStr2 = "[^0-9]+(\\d+)?$";
                Pattern pattern2 = Pattern.compile(patternStr2);
                Matcher matcher2 = pattern2.matcher(mapEntry.getKey());
                String newKeyStr = null;
                //匹配成功，提取
                if(matcher2.find()){
                    newKeyStr = matcher2.group();
                }

                //对字元素执行递归操作，区分map和list
                if(mapEntry.getValue() instanceof Map){

                    Map<String,Object> tmap = new HashMap<>();
                    targetMap.put(newKeyStr,handleMapWithNumKey((Map<String,Object>)mapEntry.getValue(),tmap));
                }else if(mapEntry.getValue() instanceof List){

                    List<Object> tlist = new ArrayList<>();
                    for(Object lmap:(List<Object>)mapEntry.getValue()){

                        if(lmap instanceof Map){
                            Map<String,Object> ttmap = new HashMap<>();
                            targetMap.put(newKeyStr,handleMapWithNumKey((Map<String,Object>)lmap,ttmap));
                        }else{
                            tlist.add(lmap);
                            targetMap.put(newKeyStr,tlist);
                        }
                    }
                }
            }else{
                targetMap.put(mapEntry.getKey(),mapEntry.getValue());
            }

        }

        return targetMap;
    }


    public static void main(String[] args){

        Map<String,Object> mapperson = new HashMap<>();
        Map<String,Object> mapschool = new HashMap<>();
        Map<String,Object> mapclass = new HashMap<>();
        Map<String,Object> mapclass2 = new HashMap<>();
        Map<String,Object> mapstu = new HashMap<>();

        List<Object> listObj = new ArrayList<>();

        mapstu.put("stu1","xuxinyu");
        mapstu.put("stu2","xuxindi");


        mapclass.put("class1",100);
        mapclass.put("class2",200);
        mapclass.put("class3",300);
        mapclass.put("class5",mapstu);

        mapclass2.put("class4",400);

        listObj.add(mapclass);
        listObj.add(mapclass2);

        mapschool.put("xiaoxue","xiao1");
        mapschool.put("xiaoxueclass",listObj);

        mapschool.put("zhongxue","zhong1");
        mapschool.put("daxue","da1");



        mapperson.put("name","xuxinyu");
        mapperson.put("age",20);
        mapperson.put("address","nanjing");
        mapperson.put("edu",mapschool);

        Map<String,Object> maptarget = convertMap(mapperson,"class5","stu3","xuxinqi",false);

        /*for(Map.Entry<String,Object> mapEntry:maptarget.entrySet()){

            if(mapEntry.getKey().equals("edu")){
                Map<String,Object> mmap = (Map<String,Object>)mapEntry.getValue();

                for(Map.Entry<String,Object> mmapEntry:mmap.entrySet()){
                    System.out.println(mmapEntry.getKey()+":   "+mmapEntry.getValue());
                }

            }


        }*/

        Map<String,Object> map1 = new LinkedHashMap<>();
        Map<String,Object> map2 = new LinkedHashMap<>();
        Map<String,Object> map3 = new HashMap<>();
        Map<String,Object> map4 = new HashMap<>();

        map4.put("1s1111",555555);


        List<Map<String,Object>> tlist = new ArrayList<>();
        tlist.add(map4);

        map3.put("111",111);
        map3.put("222",222);
        map3.put("33333mlist",tlist);

        map2.put("11",11);
        map2.put("22",22);
        map2.put("33",33);
        map2.put("44444mapp",map3);

        map1.put("1",1);
        map1.put("2",2);

        map1.put("3",3);
        map1.put("44444map",map2);

        Map<String,Object> targetMap = new HashMap<>();

        Map<String,Object> maphandle = handleMapWithNumKey(map1,targetMap);

        for(Map.Entry<String,Object> m:maphandle.entrySet()) {
            System.out.println(m.getKey()+":  "+m.getValue());
        }


    }
}

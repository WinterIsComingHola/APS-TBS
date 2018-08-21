package com.roli.common.utils.base;

import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.ManagedList;

import java.util.*;

/**
 * @author xuxinyu
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2018/2/3 22:28
 */
public class BaseTypeUtils {

    public static Map copyMap(Map a,Map b){
        a.putAll(b);
        return a;
    }


    private static Object copyStr(Object value) {
        if (value instanceof TypedStringValue) {
            TypedStringValue tsv = ((TypedStringValue) value);
            if (tsv.getTargetTypeName() != null) {
                return new TypedStringValue(tsv.getValue(),
                        tsv.getTargetTypeName());
            }
            return new TypedStringValue(tsv.getValue());
        } else if (value instanceof String) {
            return new String(value.toString());
        }
        return null;
    }

    private static String convert(Object value) {
        if (value instanceof TypedStringValue) {
            return ((TypedStringValue) value).getValue();
        } else if (value instanceof String) {
            return new String(value.toString());
        }
        return null;
    }

    public static Object convertValue(Object value) {
        String convert = convert(value);
        String copy = null;
        if (convert != null) {
            return convert;
        } else if (value instanceof Object[]) {
            Object[] arr = (Object[]) value;
            Object result[] = new Object[arr.length];
            for (int i = 0; i < arr.length; i++) {
                copy = convert(arr[i]);
                if (copy == null) {
                    return null;
                }
                result[i] = copy;
            }
            return result;
        } else if (value instanceof List) {
            List list = (List) value;
            List newList = new ManagedList();
            for (int i = 0; i < list.size(); i++) {
                copy = convert(list.get(i));
                if (copy == null) {
                    return null;
                }
                newList.add(copy);
            }
            return newList;
        } else if (value instanceof Set) {
            Set set = (Set) value;
            Set newSet = new LinkedHashSet();
            for (Object obj : set) {
                copy = convert(obj);
                if (copy == null) {
                    return null;
                }
                newSet.add(copy);
            }
            return newSet;
        } else if (value instanceof Map) {
            Map map = (Map) value;
            Map newMap = new LinkedHashMap();
            for (Object key : map.keySet()) {
                Object newKey = convert(key);
                Object newVal = convert(map.get(key));
                if (newKey == null || newVal == null) {
                    return null;
                }
                newMap.put(newKey, newVal);
            }
            return newMap;
        } else {
            return null;
        }
    }

    public static Object copyObject(Object value) {
        Object copy = copyStr(value);
        if (copy != null) {
            return copy;
        }
        if (value instanceof Object[]) {
            Object[] arr = (Object[]) value;
            Object result[] = new Object[arr.length];
            for (int i = 0; i < arr.length; i++) {
                copy = copyStr(arr[i]);
                if (copy == null) {
                    return null;
                }
                result[i] = copy;
            }
            return result;
        } else if (value instanceof List) {
            List list = (List) value;
            List newList = new ManagedList();
            for (int i = 0; i < list.size(); i++) {
                copy = copyStr(list.get(i));
                if (copy == null) {
                    return null;
                }
                newList.add(copy);
            }
            return newList;
        } else if (value instanceof Set) {
            Set set = (Set) value;
            Set newSet = new LinkedHashSet();
            for (Object obj : set) {
                copy = copyStr(obj);
                if (copy == null) {
                    return null;
                }
                newSet.add(copy);
            }
            return newSet;
        } else if (value instanceof Map) {
            Map map = (Map) value;
            Map newMap = new LinkedHashMap();
            for (Object key : map.keySet()) {
                Object newKey = copyStr(key);
                Object newVal = copyStr(map.get(key));
                if (newKey == null || newVal == null) {
                    return null;
                }
                newMap.put(newKey, newVal);
            }
            return newMap;
        } else {
            return null;
        }
    }

    /**
     *
     * @param type
     * @param value
     * @return
     */
    public static Object getValue(Class type, String value) {
        if (value == null) {
            return null;
        }
        if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value);
        }
        if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(value);
        }
        if (type == Double.class || type == double.class) {
            return Double.parseDouble(value);
        }
        if (type == Float.class || type == float.class) {
            return Float.parseFloat(value);
        }
        if (type == Character.class || type == char.class) {
            return Character.valueOf(value.charAt(0));
        }
        if (type == Byte.class || type == byte.class) {
            return Byte.valueOf(value);
        }
        if (type == Short.class || type == short.class) {
            return Short.valueOf(value);
        }
        if (type == Long.class || type == long.class) {
            return Long.valueOf(value);
        }
        if (type == String.class) {
            return value;
        }
        return null;
    }

    public static boolean checkPackage(String packages, Object bean) {
        if (packages != null) {
            boolean hit = false;
            for (String pack : packages.split(",")) {
                if (bean.getClass().getCanonicalName().startsWith(pack)) {
                    hit = true;
                    break;
                }
            }
            return hit;
        } else {
            return true;
        }
    }

    public static boolean testType(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Object[]) {
            return true;
        } else if (value instanceof List) {
            return true;
        } else if (value instanceof Set) {
            return true;
        } else if (value instanceof Map) {
            return true;
        } else if (value instanceof TypedStringValue) {
            return true;
        } else if (value instanceof String) {
            return true;
        }
        return false;
    }

    public static int countOperator(Object value) {
        int i = 0;
        if (value == null) {
            return i;
        }

        // 如果改属性存在引用信息，则不能刷新，可能造成不可预知问题
        if (value instanceof BeanDefinition) {
            i = Integer.MIN_VALUE;
        } else if (value instanceof BeanDefinitionHolder) {
            i = Integer.MIN_VALUE;
        } else if (value instanceof RuntimeBeanReference) {
            i = Integer.MIN_VALUE;
        } else if (value instanceof RuntimeBeanNameReference) {
            i = Integer.MIN_VALUE;
        }
        // ========

        else if (value instanceof Object[]) {
            for (Object obj : (Object[]) value) {
                i += countOperator(obj);
            }
        } else if (value instanceof List) {
            for (Object obj : ((List) value)) {
                i += countOperator(obj);
            }
        } else if (value instanceof Set) {
            for (Object obj : ((Set) value)) {
                i += countOperator(obj);
            }
        } else if (value instanceof Map) {
            for (Object obj : ((Map) value).keySet()) {
                i += countOperator(obj);
            }
            for (Object obj : ((Map) value).values()) {
                i += countOperator(obj);
            }
        } else if (value instanceof TypedStringValue) {
            if (((TypedStringValue) value).getValue().startsWith("${")) {
                i += 1;
            }
        } else if (value instanceof String) {
            if (((String) value).startsWith("${")) {
                i += 1;
            }
        }
        return i;
    }

}

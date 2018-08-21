package com.roli.common.spring;

import com.roli.common.utils.base.BaseTypeUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.*;
import org.springframework.util.StringValueResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xuxinyu
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2018/2/5 23:56
 */
public class ReBeanDefinitionVisitor extends BeanDefinitionVisitor {

    public ReBeanDefinitionVisitor(StringValueResolver valueResolver) {
        super(valueResolver);
    }

    protected ReBeanDefinitionVisitor() {
        super();
    }

    private static final Map<String, Map<String, PropertyValue>> pvMap = new HashMap<String, Map<String, PropertyValue>>();

    public void visitBeanDefinition(String beanName,
                                    BeanDefinition beanDefinition) {
        visitParentName(beanDefinition);
        visitBeanClassName(beanDefinition);
        visitFactoryBeanName(beanDefinition);
        visitFactoryMethodName(beanDefinition);
        visitScope(beanDefinition);
        record(beanName, beanDefinition);
        visitPropertyValues(beanDefinition.getPropertyValues());
        ConstructorArgumentValues cas = beanDefinition
                .getConstructorArgumentValues();
        visitIndexedArgumentValues(cas.getIndexedArgumentValues());
        visitGenericArgumentValues(cas.getGenericArgumentValues());
    }

    protected Map visitPropertyValues2(
            ConfigurableListableBeanFactory beanFactoryToProcess,
            BeanDefinition nbd, String beanName) {
        Map map = new HashMap();
        MutablePropertyValues pvs = nbd.getPropertyValues();
        PropertyValue[] pvArray = pvs.getPropertyValues();
        BeanWrapper bw = null;
        for (PropertyValue pv : pvArray) {
            PropertyValue bakPv = getPv(beanName, pv.getName());
            if (bakPv == null) {
                continue;
            }
            if (!BaseTypeUtils.testType(bakPv.getValue())) {
                continue;
            }
            if (BaseTypeUtils.countOperator(bakPv.getValue()) <= 0) {
                continue;
            }
            try {
                Object copyObject = BaseTypeUtils.copyObject(bakPv.getValue());
                Object newVal = resolveValue(copyObject);
                Object bean = beanFactoryToProcess.getBean(beanName);
                if (bw == null) {
                    bw = new BeanWrapperImpl(bean);
                }
                bw.setPropertyValue(pv.getName(),
                        BaseTypeUtils.convertValue(newVal));
                map.put(pv.getName(), pv.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    protected String convert(String name, String value) {
        TypedStringValue typedStringValue = new TypedStringValue(value);
        Object newVal = resolveValue(typedStringValue);
        return typedStringValue.getValue();
    }

    void record(String beanName, BeanDefinition beanDefinition) {
        if (pvMap.containsKey(beanName)) {
            return;
        }
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        PropertyValue[] propertyValues = mpv.getPropertyValues();
        Map<String, PropertyValue> pop = new HashMap<String, PropertyValue>();
        for (PropertyValue pv : propertyValues) {
            if (BaseTypeUtils.countOperator(pv.getValue()) <= 0) {
                continue;
            }
            PropertyValue opv = null;
            // if (pv.getValue() instanceof TypedStringValue) {
            // TypedStringValue tv = (TypedStringValue) pv.getValue();
            // opv = new PropertyValue(pv.getName(),
            // ConfigUtils.copyObject(tv));
            // } else {
            Object copyObject = BaseTypeUtils.copyObject(pv.getValue());

            opv = new PropertyValue(pv.getName(), copyObject);
            // }
            pop.put(opv.getName(), opv);
        }
        pvMap.put(beanName, pop);
    }

    PropertyValue getPv(String beanName, String key) {
        return pvMap.get(beanName).get(key);
    }

}

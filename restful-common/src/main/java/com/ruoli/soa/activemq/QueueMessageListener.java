package com.ruoli.soa.activemq;

import com.roli.common.utils.base.charUtil;
import com.roli.common.utils.spring.SpringContextUtil;
import com.ruoli.soa.annotation.MqHander;
import com.ruoli.soa.utils.GetClassesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.support.JmsUtils;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


/**
 * @author xuxinyu
 * @date 2018/8/28 上午11:37
 */
public class QueueMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(QueueMessageListener.class);

    @Value("${amq.annotation.package}")
    private String annPackage;

    /*
    * 消息处理规则：
    * 1、统一以Map消息进行通信，Map的第一个字段为标识字段
    * 2、消费者端的业务处理需要在@MqHander注解上标志字段，用于匹配
    * 3、业务层被反射的方法所在的类必须要存在一个无参构造函数
    * 4、业务层的被反射方法的参数只有一个Message类型
    * */


    @Override
    public void onMessage(Message message){
        MapMessage mapMessage = (MapMessage)message;
        try {

            /*
            * 1、先获取MQ消息中的tag值，这个值需要生产者和消费者双方协调一致，用于标志调用消费方的哪一个业务方法
            * 2、使用工具类获取指定包路径下的所有类的class实例
            *    注意：所有的业务层消费方法必须按照${amq.annotation.package}指定的路径建立对应类和方法
            * 3、循环所有的class实例，获取每个class实例中的所有Method方法
            * 4、循环所有的method，如果method方法存在MqHander注解并且注解的值和MQ的tag值一致，则执行反射调用
            * 5、注意：反射获取的对应的对象实例必须要从spring中获取对应的bean，所以需要保证对应的class类需要被bean化
            * */
            String tagForMethod = mapMessage.getString("tag");
            List<Class<?>> targetAnnClasses = GetClassesUtil.getAllClassesByPackageName(annPackage);
            int i = 0;
            int j = 0;
            for(Class<?> clazz : targetAnnClasses){

                Method[] methods = clazz.getDeclaredMethods();
                if(methods != null && methods.length > 0){
                    for(Method method: methods){
                        MqHander mqHander =  (MqHander)method.getAnnotation(MqHander.class);

                        if(mqHander != null){
                            if(mqHander.value().equals(tagForMethod)){
                                try {
                                    method.invoke(SpringContextUtil.getBean(charUtil.lowerFirst(clazz.getSimpleName())),message);
                                    j++;
                                } catch (IllegalAccessException e) {
                                    logger.error(e.getMessage());
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    logger.error(e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                            i++;
                        }

                    }
                }
            }

            if(i==0){
                logger.error("未找到指定注解标注，放弃处理此消息，消息TAG为："+tagForMethod);
            }
           if(j==0&&i!=0){
               logger.error("当前TAG消息未找到指定指定方法，放弃处理此消息，消息TAG为："+tagForMethod);
           }

        } catch (JMSException e) {
            throw JmsUtils.convertJmsAccessException(e);
        }
    }

}

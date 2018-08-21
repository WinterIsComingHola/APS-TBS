package com.roli.common.spring;

import com.roli.common.exception.JDBCException;
import com.roli.common.exception.SecurityException;
import com.roli.common.model.PropertiesMO;
import com.roli.common.spring.config.dao.ConfigDao;
import com.roli.common.spring.config.thread.ReloadConfigThread;
import com.roli.common.utils.base.BaseTypeUtils;
import com.roli.common.utils.net.IPAddress;
import com.roli.common.utils.security.AESHandle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringValueResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.util.*;

/**
 * 这个类提供了从数据库加载配置的实现，包括属性文件中的占位符，以及@value
 *
 * 正常情况下，spring提供的DBPlaceholderConfigurer类已经可以提供类似于@Value和${}方式读取配置项的能力
 * 但是spring的方式是从Properties中加载配置，本类讲实现从数据库中加载配置
 *
 * @author xuxinyu
 * @Title: ${file_name}
 * @Package ${package_name}
 * @date 2018/2/2 下午3:00
 *
 *
 * @see Value
 */
public class DBPropertyPlaceholderConfigurer extends
        PropertyPlaceholderConfigurer implements
        BeanPostProcessor {

    private BeanFactory beanFactory;
    private ConfigurableListableBeanFactory beanFactoryToProcess;
    private StringValueResolver valueResolver;
    private String beanName;
    private Properties dbProperties = null;

    /**
     * 自动刷新开关,默认不开启
     */
    private boolean autoReload = false;
    /**
     * 自动刷新间隔,单位毫秒，默认值60000 值不应该小于60000
     */
    private int reloadInterval = 1000 * 60;

    /**
     * debug打印 ，默认开启
     */
    private boolean debug = true;
    /**
     * 只刷新指定包路径下的属性，多个路径逗号分隔 默认刷新spring上下文中的所有类
     */
    private String packages;

    /**
     * 全局appname，可指定多个 用逗号分离，重复属性后者覆盖前者。如：public,front-all 重复属性取front-all中的定义
     */
    private String globalAppName = "PUBLIC";


    private String driverClass;

    private String name;

    private String pwd;

    private String url;

    private String profileName;

    private String appName;

    private boolean createPropertyFile;


    private List<String> appNames = new ArrayList<>();
    private Map<String, String> muiltIp;
    private ConfigDao configDao;


    private static ReloadConfigThread dt;
    {
        //非静态代码块，在构造程序时会都被调用一次。和static的区别是static只有在类加载的时候调用
        dt = new ReloadConfigThread(this);
    }

    /**
     * 整合了本类中读取配置中心表的逻辑骨架，覆盖了spring'的原始配置读取逻辑
    * @param
    * @return Properties
    * @throws
    * @author xuxinyu
    * @date 2018/2/3 22:37
    */
    @Override
    public Properties mergeProperties() {

        try{
            if(dbProperties == null){
                init();
                System.err.println(appNames.toString() + "    加载数据库配置:"
                        + profileName + ".........." + url);

                //主进程，不进行reload，新启动一个进程进行reload
                dbProperties = getDbProperties(null);

                System.err.println("读取到配置:" + dbProperties.size()
                        + "...........................");

                BaseTypeUtils.copyMap(dbProperties,super.mergeProperties());

            }else{
                BaseTypeUtils.copyMap(dbProperties,super.mergeProperties());
            }
        } catch (IOException e) {
            System.err.println("本地配置处理异常！！\t"+e);
        }

        return dbProperties;
    }



    /**
    * 在容器启动的时候，开始初始化
     * 1、获取服务器的Ip地址
     * 2、收集（用一个List）所有的appName
     * 3、初始化ConfigDAO，获得连接数据库的实例ConfigDao
    * @param
    * @return
    * @throws
    * @author xuxinyu
    * @date 2018/2/2 下午5:37
    */
    private void init(){
        try{
            muiltIp = IPAddress.getServerIp();
            mergeAppName();
            configDao = new ConfigDao(url,driverClass,pwd,name,muiltIp);

        } catch (SocketException e) {
            System.err.println("IP地址处理异常！！\t"+e);
        }

    }
    private void mergeAppName(){

        if(globalAppName!=null){
            for(String glabalappName :globalAppName.split(",")){
                if(!appNames.contains(glabalappName)){
                    appNames.add(glabalappName);
                }
            }
        }

        if(!appNames.contains(appName)){
            appNames.add(appName);
        }

    }


    /**
    * 将数据库中的配置信息转换为Properties
    * @param reload 是否实时加载配置。1==实时加载  !1==不加载
    * @return Properties 装载key和val的Properties
    * @throws
    * @author xuxinyu
    * @date 2018/2/2 下午6:02
    */
    public Properties getDbProperties(Integer reload){

        Map<String,PropertiesMO> moMap = new LinkedHashMap<>();
        try{

            for(String appName : appNames){
                moMap.putAll(configDao.queryConfig(profileName,appName,reload));
            }


        } catch (JDBCException e) {
            System.err.println("数据库处理异常！！\t"+e);
        }

        return mapToProperties(moMap);
    }

    /**
    * 提取PropertiesMO中的key和val，并以Properties的方式返回
    * @param moMap 以map形式存储的数据库配置中心的配置
    * @return Properties 存储了key和val两个字段
    * @throws
    * @author xuxinyu
    * @date 2018/2/2 下午5:50
    */
    private Properties mapToProperties(Map<String,PropertiesMO> moMap) {
        Properties properties = new Properties();
        try{

            for(PropertiesMO propertiesMO : moMap.values()){
                properties.setProperty(propertiesMO.getKey(),propertiesMO.getVal());
            }

        } catch (SecurityException e) {
            System.err.println("密文编解码处理异常！！\t"+e);
        }
        return properties;

    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        if (autoReload) {
            dt.start0();
        }
        return bean;
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }
    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        super.setBeanFactory(beanFactory);
    }
    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
        super.setBeanName(beanName);
    }

    /**
     * 刷新后置事件
     */
    private void refreshAfter(Object bean) {
        if (bean instanceof ConfigChangeCallback) {
            ((ConfigChangeCallback) bean).refreshAfter();
        }
    }

    /**
     * 刷新后置事件
     */
    private void changed(Object bean) {
        if (bean instanceof ConfigChangeCallback) {
            ((ConfigChangeCallback) bean).changed();
        }
    }

    /**
     * 刷新前置事件
     */
    private void refreshBefore(Object bean) {
        if (bean instanceof ConfigChangeCallback) {
            ((ConfigChangeCallback) bean).refreshBefore();
        }
    }


    /*
	* 用于自动刷新配置的主方法
	* 使用另外一个线程进行启动并执行此方法
	* */
    public Map refreshProperties(StringValueResolver valueResolver) {
        Map map = new HashMap();
        ReBeanDefinitionVisitor visitor = new ReBeanDefinitionVisitor(
                valueResolver);

        String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames();
        for (String curName : beanNames) {
            if (!(curName.equals(beanName) && beanFactoryToProcess
                    .equals(this.beanFactory))) {
                BeanDefinition bd = beanFactoryToProcess
                        .getBeanDefinition(curName);
                if (bd.isAbstract()) {
                    continue;
                }
                Object bean = beanFactoryToProcess.getBean(curName);
                if (!BaseTypeUtils.checkPackage(packages, bean)) {
                    continue;
                }
                if (bean instanceof ConfigChangeCallback) {
                    if (!((ConfigChangeCallback) bean).autoReload) {
                        continue;
                    }
                } else {
                    if (bean instanceof DataSource) {
                        continue;
                    }
                }
                refreshBefore(bean);
                try {
                    visitor.visitPropertyValues2(beanFactoryToProcess, bd,
                            curName);
                } catch (Exception ex) {
                    throw new BeanDefinitionStoreException(
                            bd.getResourceDescription(), curName,
                            ex.getMessage(), ex);
                }
                Field[] fields = bean.getClass().getDeclaredFields();
                boolean changed = false;
                for (Field field : fields) {
                    Value annotation = field.getAnnotation(Value.class);
                    if (annotation == null) {
                        continue;
                    }
                    ReflectionUtils.makeAccessible(field);
                    String _value = annotation.value();
                    String value = visitor.convert(curName, _value);
                    try {
                        Object object = field.get(bean);
                        if (ObjectUtils.nullSafeEquals(object,
                                BaseTypeUtils.getValue(field.getType(), value))) {
                            continue;
                        }
                        if (field.getName().equals("OPEN")) {
                            System.out.println("");
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    map.put(_value, _value);
                    changed = true;

                    ReflectionUtils.setField(field, bean,
                            BaseTypeUtils.getValue(field.getType(), value));
                }
                Method[] declaredMethods = bean.getClass().getDeclaredMethods();
                for (Method method : declaredMethods) {
                    boolean valueHit = false;
                    Value annotation = method.getAnnotation(Value.class);
                    Object[] params = new Object[method.getParameterTypes().length];
                    if (annotation != null) {
                        valueHit = true;
                        String _value = annotation.value();
                        String value = visitor.convert(curName, _value);
                        map.put(_value, _value);
                        int i = 0;
                        for (Class<?> clazz : method.getParameterTypes()) {
                            params[i++] = BaseTypeUtils.getValue(clazz, value);
                        }
                    }
                    Annotation[][] parameterAnnotations = method
                            .getParameterAnnotations();
                    int index = 0;
                    for (Annotation[] annotations : parameterAnnotations) {
                        for (Annotation annotation2 : annotations) {
                            if (annotation2 instanceof Value) {
                                valueHit = true;
                                String _value = ((Value) annotation2).value();
                                String value = visitor.convert(curName, _value);
                                params[index] = BaseTypeUtils.getValue(
                                        method.getParameterTypes()[index],
                                        value);
                                map.put(_value, _value);
                            }
                        }
                        index++;
                    }
                    if (!valueHit) {
                        continue;
                    }
                    try {
                        method.invoke(bean, params);
                        changed = true;
                    } catch (Exception e) {
                        StringBuilder builder = new StringBuilder();
                        for (Object p : params) {
                            builder.append(p + ",");
                        }
                        System.out.println("\n" + bean.getClass() + ":"
                                + method.getName() + "    params:" + builder);
                        e.printStackTrace();
                    }
                    if (changed) {
                        changed(bean);
                    }
                    refreshAfter(bean);
                }
            }
        }
        return map;
    }



    /*
	* 对外暴露两个私有内部类的逻辑
	* */
    public DBPlaceholderResolvingStringValueResolver newDBResolver(Properties props) {
        return new DBPlaceholderResolvingStringValueResolver(props);
    }

    /*
	* 在DBPropertyPlaceholderConfigurerResolver基础上，支持使用${}方式调用Properties的配置项
	* */
    private class DBPlaceholderResolvingStringValueResolver implements
            StringValueResolver{

        private final PropertyPlaceholderHelper helper;

        private final PropertyPlaceholderHelper.PlaceholderResolver resolver;

        public DBPlaceholderResolvingStringValueResolver(Properties props) {
            this.helper = new PropertyPlaceholderHelper(placeholderPrefix,
                    placeholderSuffix, valueSeparator,
                    ignoreUnresolvablePlaceholders);
            this.resolver = new DBPropertyPlaceHolderConfigurerResolver(props);
        }

        @Override
        public String resolveStringValue(String strVal) throws BeansException {
            String value = this.helper.replacePlaceholders(strVal,
                    this.resolver);
            return (value.equals(nullValue) ? null : value);
        }

    }


    /*
	* 取得Properties值的逻辑：
	* 实现PlaceholderResolver接口，PlaceholderResolver接口主要是传入一个字符串，返回一个处理过的字符串
	*
	* 初始化时候传入Properties，在方法resolvePlaceholder中传入一个Properties的key
	* 方法将会返回Properties的value
	*
	* 取得value的逻辑：传入systemPropertiesMode=1，将优先返回Properties中的配置
	* 如果没有找到配置，则去系统Properties中寻找
	* 如果依然没有找到，则去操作系统环境中寻找，如果全部找不到，将会抛出异常
	*
	*
	* */
    private class DBPropertyPlaceHolderConfigurerResolver implements PropertyPlaceholderHelper.PlaceholderResolver {

        private final Properties properties;

        private DBPropertyPlaceHolderConfigurerResolver(Properties properties){
            this.properties = properties;
        }

        @Override
        public String resolvePlaceholder(String placeHolderName){
            return DBPropertyPlaceholderConfigurer.this.resolvePlaceholder(
                    placeHolderName,properties,1);
        }
    }


    /*
	* 所有的bean的占位符处理
	* */
    @Override
    protected void doProcessProperties(
            ConfigurableListableBeanFactory beanFactoryToProcess,
            StringValueResolver valueResolver) {
        this.beanFactoryToProcess = beanFactoryToProcess;
        this.valueResolver = valueResolver;
        ReBeanDefinitionVisitor visitor = new ReBeanDefinitionVisitor(
                valueResolver);

        String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames();
        for (String curName : beanNames) {
            if (!(curName.equals(beanName) && beanFactoryToProcess
                    .equals(this.beanFactory))) {
                BeanDefinition bd = beanFactoryToProcess
                        .getBeanDefinition(curName);
                try {
                    visitor.visitBeanDefinition(curName, bd);
                } catch (Exception ex) {
                    throw new BeanDefinitionStoreException(
                            bd.getResourceDescription(), curName,
                            ex.getMessage(), ex);
                }
            }
        }
        beanFactoryToProcess.resolveAliases(valueResolver);
        beanFactoryToProcess.addEmbeddedValueResolver(valueResolver);
    }



    //===========================setter and getter====================================//


    public boolean isAutoReload() {
        return autoReload;
    }

    public void setAutoReload(boolean autoReload) {
        this.autoReload = autoReload;
    }

    public int getReloadInterval() {
        return reloadInterval;
    }

    public void setReloadInterval(int reloadInterval) {
        this.reloadInterval = reloadInterval;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getGlobalAppName() {
        return globalAppName;
    }

    public void setGlobalAppName(String globalAppName) {
        this.globalAppName = globalAppName;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        try {
            this.pwd = AESHandle.decryptAES(null,pwd);
        } catch (SecurityException e) {
            System.out.println("decrypt sercode error : "+e);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isCreatePropertyFile() {
        return createPropertyFile;
    }

    public void setCreatePropertyFile(boolean createPropertyFile) {
        this.createPropertyFile = createPropertyFile;
    }
}

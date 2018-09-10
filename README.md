# TBS--服务于接口测试
## 关于本项目
本项目主要实现了HTTP接口的MOCK和客户端请求
你可以任意的定义一个接口，包括：接口名称、接口路径、接口的请求方法，一旦完成这些基本配置，系统将自动完成MOCK服务，可以同时支持JSON/JSONP/XML三种方式的MOCK返回。

你可以定义请求远端的任意可用接口的参数，以POST或者GET的方式，并以JSON的格式进行展示

## 演示地址
http://106.14.224.255:18082/front/

## 技术和架构
### 技术栈
后端：基于SSM三大框架进行构建，集成了MQ和Redis。

前端：基于Layui。相对而言，这是一个对后端程序员比较友好的前端框架。

### 整体结构
整个代码结构的构建分为两层结构，底层封装了很多基于原生API的工具类以及springbean等等，主要便于保证上层业务代码的规范和整洁。主要集中于public-common和restful-common这两个包内
> common层实现了一个简易的基于spring的配置中心功能，所有的配置项全部在数据库中进行配置，支持分环境和分应用进行配置。

基于底层的封装，业务层主要有两个应用系统：aps-front和aps-soa.soa负责具体的业务逻辑以及和数据层进行交互，并以http协议接口的形式对外提供服务。front则负责前端数据的转发、渲染。在需要的时候和soa进行说数据交互
## 部署方法
### 初始化数据库
首选需要新建数据库，点击[initDB.zip](http://106.14.224.255:10803/xuxyweb/initDB.zip)文件。下载后解压并执行包内的sql完成数据初始化

> 1. 在config_profiles.sql文件中，已经默认写入了所有需要的配置信息，其中，有一些配置请根据实际的环境信息进行修改
jdbc.url_rest
jdbc.username_rest
jdbc.password_rest
jdbc.url_usr
jdbc.username_usr
jdbc.password_usr
soa.path
mainDomain
miscDomain
amq.url/username/password/topic.name/queue.name/annotation.package
websocket.sessionid
wsDomain
> 2. soa_rest_oauth中的数据用于服务之间的接口调用鉴权，请将FRONT服务的对应服务器的主机名和IP地址配入这个表中（除了HOST_NAME和IP字段以外的可以随意填入）


### 搭建ActiveMQ服务
请自行到官网参考MQ的搭建步骤
### 打包部署到应用服务器
1. 拉取代码到本地后，切换到对应目录下后执行maven命令生成对应的war包：
mvn clean package -DskipTests -P production
> production是定义在项目顶级pom文件（位于public-pom目录下）中的重要profile配置，因为默认的是dev，如果不加-P参数会导致获取的应用配置不是initDB中定义的配置，启动容器的时候会有数据库连接的异常。
>当然你也可以随意的按照自己的需求进行配置的修改，切记，顶级pom文件中的数据库密码数据经过了AES加密，默认的密钥内容为空字符串("")，请务必输入可以正常进行AES解码的密码密文。示例如下：

```
\\\\\\\<profile>
            <id>production</id>
            <properties>
                <active.profilename>production</active.profilename>
                <active.driverClass>com.mysql.jdbc.Driver</active.driverClass>
                <active.name>ddldata</active.name>
                <active.pwd>s2jGEMUUrmQxN2TCSZD0Rg==</active.pwd>
                <active.url>jdbc:mysql://localhost:3306/config-center?characterEncoding=utf8<![CDATA[&amp;]]>useSSL=false</active.url>
                <active.createPropertyFile>false</active.createPropertyFile>
                <active.autoReload>true</active.autoReload>
            </properties>
        </profile>
```



2. 将生成的war包部署至应用服务器的对应目录，比如tomcat/jetty。
在部署完成后，启动容器并浏览器测试是否访问正常

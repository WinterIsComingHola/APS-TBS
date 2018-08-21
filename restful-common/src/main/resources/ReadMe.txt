
整体的调用、鉴权处理如下：

1、设定basesoaparam类，此类主要承载基本属性
2、设定设定basesoaparam类的子类soaparam，用于设定业务层的属性
业务层只有一个String，这个String必须是json格式
一般使用JsonUtil将传入的参数转换为String（如果是单个值可以统一使用MAP，如果是对象则可以直接转换）

3、对业务层的soaparam进行解析，去除带有igrone的属性
将剩余的属性转换为字符串
4、使用加密工具对字符串加密生成密文，并把密文放入basesoaparam类的sign字段

5、再把整个basesoaparam类进行toString处理，放入basesoaparam的PARAM_MARK属性


提交发送


解析：
将request的PARAM_MARK属性取出，获取String属性
解析String属性为basesoaparam类
逐个解析，若解析正常，则返回true，否则返回false




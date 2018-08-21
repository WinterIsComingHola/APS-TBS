package com.roli.common.utils.net;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuxinyu
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2018/2/2 下午4:44
 */
public class IPAddress {



    /**
    * 获取服务器的iP地址，并以Map形式返回，会优先寻找外网ip地址
    * @param
    * @return Map
    * @throws
    * @author xuxinyu
    * @date 2018/2/2 下午4:45
    */
    public static Map<String,String> getServerIp() throws SocketException {

        Map<String, String> mapIp = new HashMap<>();

        InetAddress ip;
        //获取所有的网络接口
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

        //默认没有外网ip
        boolean isPublicIp = false;

        //遍历机器上所有的网卡，需要明确是否找到外网ip，如果找到外网ip，则直接退出循环，使用这个外网ip
        while (networkInterfaces.hasMoreElements()&&!isPublicIp){
            NetworkInterface networkInterface = networkInterfaces.nextElement();

            //获取这个网卡的所有ip地址
            Enumeration<InetAddress>  ipAddress = networkInterface.getInetAddresses();

            //遍历所有的ip地址
            while (ipAddress.hasMoreElements()){

                ip = ipAddress.nextElement();

                /*
                * 如果ip不是本机本地地址，也不是回环地址，且不是ip6格式，则认为是外网地址
                * */
                if(!ip.isLoopbackAddress() && !ip.isSiteLocalAddress() &&
                        ip.getHostAddress().indexOf(":")==-1){
                    mapIp.put(ip.getHostAddress(), ip.getHostAddress());
                    isPublicIp = true;//改成true后，整个while循环将不再执行
                    break;
                }else if(!ip.isLoopbackAddress() && ip.isSiteLocalAddress() &&
                        ip.getHostAddress().indexOf(":")==-1){
                    //内网ip则需要把所有的循环遍历完成

                    mapIp.put(ip.getHostAddress(), ip.getHostAddress());
                }
            }
        }

        return mapIp;
    }


    public static void main(String[] args) throws SocketException {

        Map<String,String> mapip = getServerIp();

        System.out.println(mapip.toString());

    }

}

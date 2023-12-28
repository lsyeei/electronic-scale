package cn.devhome.app.utils;

import purejavacomm.CommPortIdentifier;

import java.util.ArrayList;
import java.util.Enumeration;

/**
 * CommPortUtils
 *
 * @author lishiying
 * @version 1.0.0
 * @date 2023-12-27 16:25
 **/
public class CommPortUtils {

    /**
     * 查找电脑上所有可用 com 端口
     * @return 可用端口名秤列表，没有时 列表为空
     */
    public static final ArrayList<String> findSystemAllComPort() {
        // getPortIdentifiers：获得电脑主板当前所有可用串口
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        ArrayList<String> portNameList = new ArrayList<>();
        // 将可用串口名添加到 List 列表
        while (portList.hasMoreElements()) {
            //名称如 COM1、COM2....
            String portName = portList.nextElement().getName();
            portNameList.add(portName);
        }
        return portNameList;
    }
}

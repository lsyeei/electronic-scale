package cn.devhome.app.service;

import cn.devhome.app.domain.ComPortConfig;

/**
 * IComPort
 * 串口
 * @author lishiying
 * @version 1.0.0
 * @date 2023-12-26 14:37
 **/
public interface IComPort {
    /**
     * 打开串口
     * @param config 串口配置
     * @return true 成功， false 失败
     */
    boolean open(ComPortConfig config);

    /**
     * 串口是否已打开
     * @return true 打开， false 未打开
     */
    boolean isOpen();

    /**
     * 关闭串口
     * @return true 成功， false 失败
     */
    boolean close();

    /**
     * 设置数据接收者
     * @param processor 数据接收者
     */
    void setReceiver(IProcessor processor);
}

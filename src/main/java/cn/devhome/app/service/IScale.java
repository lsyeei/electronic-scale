package cn.devhome.app.service;

import cn.devhome.app.domain.ComPortConfig;
import cn.devhome.app.domain.ScaleInfo;
import cn.devhome.app.domain.ScaleValue;

/**
 * IScale
 * 电子秤接口
 * @author lishiying
 * @version 1.0.0
 * @date 2023-12-26 14:48
 **/
public interface IScale {
    /**
     * 连接电子秤
     * @param config 串口配置
     * @return true 成功， false 失败
     */
    boolean connect(ComPortConfig config);

    /**
     * 电子秤是否连接
     * @return true 打开， false 未打开
     */
    boolean isConnected();

    /**
     * 断开与电子秤的连接
     * @return true 成功， false 失败
     */
    boolean disconnect();

    /**
     * 读取当前秤值
     * @return 秤值
     */
    ScaleValue getScaleValue();

    /**
     * 设置电子秤信息
     * @param info 电子秤信息
     */
    void setScaleInfo(ScaleInfo info);
}

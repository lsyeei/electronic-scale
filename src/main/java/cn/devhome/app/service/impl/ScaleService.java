package cn.devhome.app.service.impl;

import cn.devhome.app.domain.ComPortConfig;
import cn.devhome.app.domain.EventData;
import cn.devhome.app.domain.ScaleInfo;
import cn.devhome.app.domain.ScaleValue;
import cn.devhome.app.enums.EventDataType;
import cn.devhome.app.service.IComPort;
import cn.devhome.app.service.IProcessor;
import cn.devhome.app.service.IScale;
import cn.devhome.app.utils.EventPublisher;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * ScaleService
 *
 * @author lishiying
 * @version 1.0.0
 * @date 2023-12-26 14:20
 **/
@Slf4j
public class ScaleService implements IScale, IProcessor {
    /**
     * 电子秤当前值
     */
    private ScaleValue currentValue;
    /**
     * 串口
     */
    private IComPort comPort;
    /**
     * 电子秤信息
     */
    private ScaleInfo scaleInfo;

    private ByteBuffer buffer = ByteBuffer.allocate(128);

    public ScaleService(ScaleInfo scale) {
        setScaleInfo(scale);
        comPort = new DefaultComPort();
        this.currentValue = new ScaleValue();
    }

    @Override
    public boolean connect(ComPortConfig config) {
        comPort.setReceiver(this);
        return comPort.open(config);
    }

    @Override
    public boolean isConnected() {
        return comPort.isOpen();
    }

    @Override
    public boolean disconnect() {
        return comPort.close();
    }

    @Override
    public ScaleValue getScaleValue() {
        return currentValue;
    }

    @Override
    public void setScaleInfo(ScaleInfo info) {
        scaleInfo = info;
    }

    @Override
    public void process(InputStream stream) {
        byte[] data = new byte[12];
        try {
            // 调用一次数据解析
            scaleInfo.getProxy().parseData(stream);
        }catch (Exception e){
            EventPublisher.publish(new EventData(EventDataType.TIPS, e.getMessage().replaceAll("java.lang.Exception:","")));
            log.error("读秤值出现异常，异常代码：{}", ExceptionUtils.getStackTrace(e));
            return;
        }

        // 获取秤读数
        currentValue = scaleInfo.getProxy().getValue();
        System.out.println("获取秤值：" + JSON.toJSONString(currentValue));
        // 发送秤值
        EventPublisher.publish(new EventData(EventDataType.SCALE_VALUE, currentValue));
    }

}

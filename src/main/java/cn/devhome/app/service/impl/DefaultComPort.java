package cn.devhome.app.service.impl;

import cn.devhome.app.domain.ComPortConfig;
import cn.devhome.app.domain.EventData;
import cn.devhome.app.enums.EventDataType;
import cn.devhome.app.service.IComPort;
import cn.devhome.app.service.IProcessor;
import cn.devhome.app.utils.EventPublisher;
import cn.devhome.app.view.AlertDlg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import purejavacomm.CommPortIdentifier;
import purejavacomm.SerialPort;
import purejavacomm.SerialPortEvent;
import purejavacomm.SerialPortEventListener;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;

/**
 * DeaultComPort
 *
 * @author lishiying
 * @version 1.0.0
 * @date 2023-12-26 14:39
 **/
@Slf4j
public class DefaultComPort implements IComPort, SerialPortEventListener {
    /**
     * 串口
     */
    private SerialPort serialPort;
    private CommPortIdentifier commPortIdentifier;
    private InputStream input;
    private OutputStream output;
    private IProcessor processor;
    /**
     * 串口打开状态
     */
    private boolean connectStatus = false;

    /**
     * 打开串口
     * @param config 配置
     * @return true 成功，false 失败
     */
    @Override
    public boolean open(ComPortConfig config) {
        String portName = config.getPortName();
        try {
            commPortIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        }catch (Exception e){
            log.error("端口{}不存在", portName);
            AlertDlg.message("串口" + portName + "不存在");
            return false;
        }
        try {
            serialPort = (SerialPort) commPortIdentifier.open(portName, 5000);
        }catch (Exception e){
            log.error("打开串口{}失败", portName);
            AlertDlg.message("串口" + portName + "打开失败");
            return false;
        }
        try {
            serialPort.setSerialPortParams(config.getBaud(), config.getDataBits(), config.getStopBits(), config.getParity());
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        }catch (Exception e){
            log.error("串口{}参数设置失败", portName);
            AlertDlg.message("串口" + portName + "连接参数设置失败");
            serialPort.close();
            return false;
        }
        try {
            serialPort.addEventListener(this);
        }catch (Exception e){
            log.error("串口{}监听失败", portName);
            AlertDlg.message("串口" + portName + "监听失败");
            serialPort.close();
            return false;
        }
        serialPort.notifyOnDataAvailable(false);
        serialPort.setInputBufferSize(1024);
        try {
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
        }catch (Exception e){
            log.error("获取串口{}的输入/输出失败", portName);
            AlertDlg.message("串口" + portName + "获取输入失败");
            serialPort.close();
            return false;
        }
        log.trace("串口打开成功");
        EventPublisher.publish(new EventData(EventDataType.TIPS, "串口打开成功"));
        connectStatus = true;

        // 监控输入数据
        monitorInput();

        return true;
    }

    /**
     * 关闭串口
     */
    @Override
    public boolean close(){
        if (serialPort != null) {
            serialPort.close();
            connectStatus = false;
            log.info("关闭串口");
            EventPublisher.publish(new EventData(EventDataType.TIPS, "串口关闭"));
            return true;
        }
        connectStatus = false;
        return false;
    }

    @Override
    public boolean isOpen() {
        return connectStatus;
    }

    @Override
    public void setReceiver(IProcessor processor) {
        this.processor = processor;
    }

    /**
     * 监控输入数据
     */
    private void monitorInput(){
        CompletableFuture.runAsync(()->{
            while (connectStatus) {
                try {
                    if (input.available() > 0) {
                        processor.process(input);
                    }
                }catch (Exception e){
                    log.error("available()调用出现异常，异常代码：{}", ExceptionUtils.getStackTrace(e));
                }
            }
        }).exceptionally(e->{
            log.error("读数据出现异常，异常：{}", ExceptionUtils.getStackTrace(e));
            // 重启线程
            monitorInput();
            return null;
        });
    }

    /**
     * 串口事件处理
     * @param serialPortEvent 事件
     */
    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        int eventType = serialPortEvent.getEventType();
        switch (eventType){
            case SerialPortEvent.DATA_AVAILABLE:
                processor.process(input);
                break;
            case SerialPortEvent.BI:
                log.error("通信中断");
                EventPublisher.publish(new EventData(EventDataType.TIPS, "通信中断"));
                break;
            case SerialPortEvent.FE:
                log.error("帧错误");
                EventPublisher.publish(new EventData(EventDataType.TIPS, "帧错误"));
                break;
            case SerialPortEvent.PE:
                log.error("校验错误");
                EventPublisher.publish(new EventData(EventDataType.TIPS, "校验错误"));
                break;
            case SerialPortEvent.OE:
                log.error("溢位错误");
                EventPublisher.publish(new EventData(EventDataType.TIPS, "溢位错误"));
                break;
            default:
        }
    }
}

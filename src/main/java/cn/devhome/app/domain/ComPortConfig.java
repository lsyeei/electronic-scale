package cn.devhome.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import purejavacomm.SerialPort;

import java.util.*;

/**
 * 串口配置
 * @author lishiying
 * @date 2022/8/2 002
 */
@Data
@Accessors(chain = true)
public class ComPortConfig {
    /**
     * 端口号
     */
    private String portName;
    /**
     * 波特率
     */
    private Integer baud;
    /**
     * 数据位
     */
    private Integer dataBits;
    /**
     * 奇偶位
     */
    private Integer parity;
    /**
     * 停止位
     */
    private Integer stopBits;

    /**
     * 串口支持的校验类型
     */
    private static Map<Integer, String> parityMap = new HashMap<>(5);
    private static Map<Integer, String> stopBitsMap = new HashMap<>(3);
    private static Integer[] baudList = {2400, 4800, 9600, 14400, 19200, 34800, 56000, 57600, 115200};
    static {
        parityMap.put(SerialPort.PARITY_NONE, "None");
        parityMap.put(SerialPort.PARITY_ODD, "奇校验");
        parityMap.put(SerialPort.PARITY_EVEN, "偶校验");
        parityMap.put(SerialPort.PARITY_MARK, "Mark");
        parityMap.put(SerialPort.PARITY_SPACE, "Space");

        stopBitsMap.put(SerialPort.STOPBITS_1, "1位");
        stopBitsMap.put(SerialPort.STOPBITS_2, "2位");
        stopBitsMap.put(SerialPort.STOPBITS_1_5, "1.5位");
    }

    /**
     * 获取串口支持的校验类型
     * @return 校验类型列表
     */
    public static List<PairData> allParity(){
        List<PairData> result = new ArrayList<>(parityMap.size());
        for (Map.Entry<Integer, String> entry : parityMap.entrySet()) {
            result.add(new PairData(entry.getValue(), entry.getKey()));
        }
        return result;
    }
    /**
     * 获取串口支持的停止位
     * @return 停止位列表
     */
    public static List<PairData> allStopBits(){
        List<PairData> result = new ArrayList<>(stopBitsMap.size());
        for (Map.Entry<Integer, String> entry : stopBitsMap.entrySet()) {
            result.add(new PairData(entry.getValue(), entry.getKey()));
        }
        return result;
    }

    /**
     * 获取串口支持的波特率
     * @return 波特率列表
     */
    public static Integer[] allBaud(){
        return baudList;
    }

    /**
     * 获取当前校验类型
     * @return 校验类型
     */
    public PairData obtainParity(){
        if (parity == null){
            return null;
        }
        return new PairData(parityMap.get(parity), parity);
    }
    /**
     * 获取当前停止位
     * @return 停止位
     */
    public PairData obtainStopBits(){
        if (stopBits == null){
            return null;
        }
        return new PairData(stopBitsMap.get(stopBits), stopBits);
    }
}

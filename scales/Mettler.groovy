import java.nio.ByteBuffer

/**
 * Mettler 
 *
 * @author lishiying
 * @date 2023-12-27 16:06
 * @version 1.0.0
 */
class Mettler {

     public final String name = "梅特勒-托利多";
     public final String code = "Mettler scale";

     /**
     * 电子秤读数
     */
    private String value;
    /**
     * 单位
     */
    private String unit;

    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private boolean errFlag = false;
    byte[] lastData = new byte[16];
    byte[] temp = new byte[1024];

    void receiveData(InputStream stream) {
        try {
            int available = stream.available();
            int count = 0;
            int read = 0;
            while(available - count > 0){
                read = stream.read(temp);
                count += read;
                if (read <= 0) {
                    return;
                }
                int index = 0;
                while (index < read) {
                    // buffer剩余空间
                    int left = buffer.limit() - buffer.position();
                    if (left <= read - index) {
                        buffer.put(temp, index, left);
                        index += left;
                        consumeData();
                    } else {
                        buffer.put(temp, index, read - index);
                        index += read;
                    }
                }
                consumeData();
            }
        }catch (Exception e){
            throw new Exception("读秤值出现异常," + e.getMessage());
        }
    }

    /**
     * 数据比较
     * @param lastData 原数据
     * @param data 目标数据
     * @return true 相同，false 不同
     */
    private boolean dataEqual(byte[] lastData, byte[] data) {
        boolean flag = true;
        for (int i=0;i<16;i++){
            if (lastData[i]!=data[i]){
                flag = false;
                lastData[i] = data[i];
            }
        }
        return flag;
    }

    /**
     * 从缓存中取数据
     */
    private void consumeData(){
        byte[] data = new byte[16];
        buffer.flip();
        while (buffer.limit() - buffer.position() >= 17) {
            byte head = buffer.get();
            if (head == 0x02) {
                buffer.get(data, 0, 16);
                if (dataEqual(lastData, data)){
                    continue;
                }
                parseData(data);
            }
        }
        buffer.compact();
    }
    /**
     * 解析串口数据
     * @param data 数据
     */
    private void parseData(byte[] data) throws Exception {
        if (data[15] != 0x0D){
            throw new Exception("数据格式错误，数据内容");
        }
        // 状态字节A
        int stateA = getState(data,0);
        // 状态字节B
        int stateB = getState(data,1);
        if ((stateB & 0x01) == 0){
            // 当前为毛重，不计算
            throw new Exception("当前数据为毛重数据，请调整为净重数据");
            errFlag = true;
        }else if (errFlag){
            errFlag = false;
        }
        // 状态字节C
        int stateC = getState(data,2);
        // 显示的重量
        int weight = getWeight(data,3,8);
        // 皮重
        int tare = getWeight(data,9,14);
        // 小数位处理
        int fraction = stateA & 0x07;
        double value = weight * Math.pow(10,(2 - fraction));
        // 判断正负
        if ((stateB & 0x02) == 0x02){
            value = 0 - value;
        }
        if (fraction <= 2) {
            this.value = String.format("%d",(int)value);
        }else{
            this.value = String.format("%." + (fraction - 2) + "f",value);
        }
        switch (stateC & 0x07){
            case 0:
                if ((stateB & 0x10) == 0x10){
                    this.unit = "千克";
                }else {
                    this.unit = "磅";
                }
                break;
            case 1:
                this.unit = "克";
                break;
            case 3:
                this.unit = "盎司";
                break;
            default:
                throw new Exception("状态字节C=" + stateC + ",无法识别");
        }
    }

    private int getState(byte[] data, int i) {
        return data[i];
    }

    /**
     * 将指定位置的值转换为数字
     * @param data 数组
     * @param start 起始位置
     * @param end 结束位置
     * @return 数字
     */
    private int getWeight(byte[] data, int start, int end) {
        int result = 0;
        for (int i = start; i <= end; i++){
            result = result * 10;
            if (data[i] != 0x20) {
                result = result + (data[i] - 0x30);
            }
        }
        return result;
    }
}
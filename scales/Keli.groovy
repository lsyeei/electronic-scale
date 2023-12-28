import java.nio.ByteBuffer

/**
 * Keli 
 *
 * @author lishiying
 * @date 2023-12-26 16:19
 * @version 1.0.0
 */
class Keli {
    private final String name = "宁波柯力";
    private final String code = "KE_LI_SCALE";

    /**
     * 电子秤读数
     */
    private String value;
    /**
     * 单位
     */
    private String unit;

    private ByteBuffer buffer = ByteBuffer.allocate(128);

    void receiveData(InputStream stream) throws Exception {
        byte[] data = new byte[12];
        while (stream.available() > 0) {
            int read = stream.read(data);
            if (read == -1) {
                return;
            }
            buffer.put(data, 0, read);
            while (buffer.position() >= 12) {
                buffer.flip();
                byte head = buffer.get();
                if (head == 0x02) {
                    buffer.get(data, 0, 11);
                    parseData(data);
                }
                buffer.compact();
            }
            break;
        }
    }

    /**
     * 解析串口数据
     * @param data 数据
     */
    private void parseData(byte[] data) throws Exception {
        if (data[10] != 0x03){
            throw new Exception("电子秤数据格式错误");
        }
        if (!checkData(data)){
            throw new Exception("电子秤数据校验错误");
        }
        int weight = getWeight(data, 1, 6);
        // 小数位处理
        int fraction = data[7] & 0x0F;
        double value = weight * Math.pow(10,(0 - fraction));
        // 判断正负
        if (data[0] == 0x2D){
            value = 0 - value;
        }
        if (fraction == 0) {
            this.value= String.format("%d",(int)value);
        }else{
            this.value= String.format("%." + fraction + "f",value);
        }
        this.unit= "千克";
    }

    /**
     * 校验数据
     * data 的索引8，9分别对应异或校验的高四位和第四位
     * data的异或校验算法：从索引0到7依次做异或操作，校验和的高四位和第四位分别抽取出来处理
     * 高四位（第四位）小于等于9时 加 0x30，大于9时加 0x37
     * @param data 数据
     * @return true 正确，false 错误
     */
    private boolean checkData(byte[] data) {
        int verifyData = data[0];
        // 异或运算
        for (int i = 1; i <= 7; i++){
            verifyData ^= data[i];
        }
        // 运算结果处理
        int high = (verifyData & 0xF0) >> 4;
        int low = verifyData & 0x0F;
        if (high <= 9){
            high += 0x30;
        } else {
            high += 0x37;
        }
        if (low <= 9){
            low += 0x30;
        } else {
            low += 0x37;
        }
        // 校验
        if (high != data[8] || low != data[9]){
            return false;
        }
        return true;
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
            result = result * 10 + (data[i] & 0x0F);
        }
        return result;
    }
}
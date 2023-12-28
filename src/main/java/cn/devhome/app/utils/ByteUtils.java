package cn.devhome.app.utils;

/**
 * @author lishiying
 * @version 1.0
 * @date 2021/9/2 14:30
 */
public class ByteUtils {
    /**
     * 字节数组转为16进制字符串
     * @param data 字节流
     * @return 转换后的字符串
     */
    public static String bytesToString(byte[] data){
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(data[i] & 0xFF);
            if (hex.length() < 2){
                result.append("0");
            }
            result.append(hex);
        }
        return result.toString();
    }
    /**
     * 字节流转为字符串
     * @param data 字节流
     * @return 转换后的字符串
     */
    public static String encodeBytes(byte[] data, String charsetName){
        if (data == null){
            return null;
        }
        String str = "";
        try{
            str = new String(data, charsetName);
            str = str.trim();
        }catch (Exception e){
            return null;
        }
        return str;
    }

    /**
     * 整形转十六进制字符串
     * @param n 整形数值
     * @return 十六进制字符串
     */
    public static String byteToHexString(int n) {
        String[] hexArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        if (n < 0) {
            n = n + 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexArray[d1] + hexArray[d2];
    }
}

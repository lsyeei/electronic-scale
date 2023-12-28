import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Sudofan 
 *
 * @author lishiying
 * @date 2023-12-27 16:09
 * @version 1.0.0
 */
class Sudofan {

    public final String name = "苏多凡";
    public final String code = "Sudofan scale";

    /**
     * 电子秤读数
     */
    private String value;
    /**
     * 单位
     */
    private String unit;
    /**
     * 串口模式串
     */
    private final Pattern pattern = Pattern.compile("(OL|ST|US),(TR|NT|GS),([+-])\\s*(\\d+\\.\\d+)(kg|g|lb|pcs)");

    void receiveData(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        scanner.useDelimiter("\r\n");
        while (scanner.hasNext()){
            String next = scanner.next();
            parseData(next);
            break;
        }
    }

    private void parseData(String data) {
        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()){
            // 格式不匹配，丢弃
            return;
        }
        String head1 = matcher.group(1);
        if ("OL".equals(head1)){
            // 过载，丢弃数据
            return;
        }
        // 正负
        String symbol = matcher.group(3);
        // 数值
        String valueStr = matcher.group(4);
        if ("-".equals(symbol)){
            valueStr = "-" + valueStr;
        }
        value = valueStr;
        // 单位
        String unitPart = matcher.group(5);
        if ("kg".equals(unitPart)){
            this.unit = "千克";
        } else if ("g".equals(unitPart)){
            this.unit = "克";
        } else if ("lb".equals(unitPart)){
            this.unit = "磅";
        }
    }
}

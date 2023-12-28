package cn.devhome.app.service.impl;

import cn.devhome.app.domain.ScaleInfo;
import cn.devhome.app.service.ScaleMarket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * ScaleServiceTest
 *
 * @author lishiying
 * @version 1.0.0
 * @date 2023-12-27 14:58
 **/
class ScaleServiceTest {
    private ScaleMarket scales;
    private final String path = "../../scales";
    private ScaleService scaleService;
    private InputStream stream;
    @BeforeEach
    void setUp() throws Exception {
        // 初始化脚本文件
        scales = new ScaleMarket(path);
        scales.init();
        List<ScaleInfo> allScale = scales.getAllScale();
        Assert.noNullElements(allScale,"未获取到脚本文件");
        // 构造scaleService类
        scaleService = new ScaleService(scales.getScaleByName("宁波柯力"));
        // 构造数据
        mockData();

    }

    private void mockData() {
        int[] sample = {50, 68, 79, 80, 45, 58, 74, 80, 69, 82};
        int length = sample.length;
        ByteBuffer buffer = ByteBuffer.allocate(12 * length);
        for (int i = 0; i < length; i++) {
            buffer.put(genData(sample[i]));
        }
        byte[] data = buffer.array();
        stream = new ByteArrayInputStream(data);
    }

    private byte[] genData(int num) {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        // 头标识
        buffer.put((byte)0x02);
        // 正负
        buffer.put((byte)'+');
        // 读数
        buffer.put(String.format("%06d", num).getBytes());
        // 小数点位置
        buffer.put((byte) 0);
        // 校验值
        calcSum(buffer);
        // 结束标识
        buffer.put((byte) 0x03);
        return buffer.array();
    }

    private void calcSum(ByteBuffer buffer) {
        byte verifyData = buffer.get(1);
        // 异或运算
        for (int i = 1; i <= 7; i++){
            verifyData ^= buffer.get(i + 1);
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
        buffer.put((byte) high);
        buffer.put((byte) low);
    }

    @Test
    void processTest() {
        scaleService.process(stream);
    }
}
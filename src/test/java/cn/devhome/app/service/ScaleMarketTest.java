package cn.devhome.app.service;

import cn.devhome.app.domain.ScaleInfo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.List;

/**
 * ScaleMarketTest
 *
 * @author lishiying
 * @version 1.0.0
 * @date 2023-12-27 09:02
 **/
public class ScaleMarketTest {
    private ScaleMarket scales;
    private final String path = "../../scales";
    @Before
    public void init() throws Exception{
        scales = new ScaleMarket(path);
        scales.init();
    }
    @Test
    public void getAllScaleTest(){
        System.out.println("电子秤脚本测试路径：" + path);
        List<ScaleInfo> allScale = scales.getAllScale();
        Assert.noNullElements(allScale,"未获取到脚本文件");
        for (ScaleInfo info : allScale) {
            System.out.println(info.toString());
        }
        System.out.println("电子秤脚本加载测试完成");
    }
}
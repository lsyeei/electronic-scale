package cn.devhome.app.service;

import java.io.InputStream;

/**
 * IProcesser
 * 数据处理器
 * @author lishiying
 * @version 1.0.0
 * @date 2023-12-26 15:03
 **/
public interface IProcessor {
    /**
     * 处理输入的数据
     * @param stream 数据流
     */
    void process(InputStream stream);
}

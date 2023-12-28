package cn.devhome.app.service;

import cn.devhome.app.domain.ScaleValue;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.*;
import java.nio.charset.Charset;

/**
 * ScaleProxy
 * 电子秤代理
 * @author lishiying
 * @version 1.0.0
 * @date 2023-12-26 17:00
 **/
public class ScaleProxy {
    /**
     * 脚本文件
     */
    private String scriptFile;
    /**
     * groovy 实例
     */
    GroovyObject scriptInstance;
    /**
     * 秤的名称
     */
    private String scaleName;
    /**
     * 秤标识码
     */
    private String scaleCode;
    /**
     * 秤读数
     */
    private ScaleValue value = new ScaleValue();

    public ScaleProxy(String scriptFile) throws Exception {
        this.scriptFile = scriptFile;
        loadScript(scriptFile);
    }
    public ScaleProxy(File file) throws Exception {
        this.scriptFile = file.getPath();
        loadScript(file);
    }

    public String getScaleName(){
        return scaleName;
    }

    public String getScaleCode() {
        return scaleCode;
    }

    public ScaleValue getValue() {
        return value;
    }

    /**
     * 从数据流中解析一次秤读数
     * @param stream 数据流
     * @return 秤读数
     */
    public ScaleValue parseData(InputStream stream){
        scriptInstance.invokeMethod("receiveData", new Object[]{stream});
        value.setValue((String)scriptInstance.getProperty("value"));
        value.setUnit((String)scriptInstance.getProperty("unit"));
        return value;
    }

    /**
     * 加载groovy 脚本
     * @param scriptFile 脚本文件
     */
    private void loadScript(String scriptFile) throws Exception{
        loadScript(new File(scriptFile));
    }
    private void loadScript(File file) throws Exception{
        // 读取文件
        if (!file.exists()){
            // 文件不存在
            throw new Exception("文件" + scriptFile + "不存在");
        }
        byte[] buf = new byte[(int)file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(buf);
            fileInputStream.close();
//            FileReader reader = new FileReader(file);
//            reader.read(buf);
//            reader.close();
        } catch (Exception e) {
            throw new Exception("载入脚本" + scriptFile + "出错");
        }
        // 加载脚本
        GroovyClassLoader loader = new GroovyClassLoader();
        String script = new String(buf, Charset.forName("utf-8")).trim();
        Class scriptClass = loader.parseClass(script);
        // 实例化
        scriptInstance = (GroovyObject) scriptClass.getDeclaredConstructor().newInstance();
        // 获取秤信息
        scaleName = (String)scriptInstance.getProperty("name");
        scaleCode = (String)scriptInstance.getProperty("code");
    }
}

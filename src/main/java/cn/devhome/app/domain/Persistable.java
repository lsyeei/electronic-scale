package cn.devhome.app.domain;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;

/**
 * 持久化
 * @author lishiying
 * @version v1.0
 * @date 2021/6/25 13:01
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
@Slf4j
public class Persistable implements Serializable {
    /**
     * 加载
     * @param <T> 子类型
     * @param file 存储文件
     * @param clazz 要返回的类
     * @return 对象,null表示加载失败
     */
    public static <T extends Persistable> T load(String file, Class<T> clazz) {
        T object = null;
        // 检测文件
        File fileObject = new File(file);
        if (!fileObject.exists()){
            log.error("配置文件{}不存在", file);
            return null;
        }
        try {
            FileReader reader = new FileReader(file);
            char[] buf = new char[(int)fileObject.length()];
            reader.read(buf);
            object = JSON.parseObject(String.valueOf(buf), clazz);
            reader.close();
        } catch (Exception e) {
            log.error("载入配置{}出错，错误详情：{}", file, ExceptionUtils.getStackTrace(e));
        }
        return object;
    }

    /**
     * 保存配置
     * @param file 存储文件
     * @return  true 成功，false 失败
     */
    public boolean save(String file) {
        try {
            File fObject = new File(file);
            if (!fObject.exists()){
                // 文件不存在，创建一个
                fObject.createNewFile();
            }
            FileWriter writer = new FileWriter(fObject);
            writer.write(JSON.toJSONString(this));
            writer.close();
        } catch (Exception e) {
            log.error("保存配置{}出错，错误详情：{}", file, ExceptionUtils.getStackTrace(e));
            return false;
        }
        return true;
    }
}

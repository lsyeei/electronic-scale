package cn.devhome.app.service;

import cn.devhome.app.domain.ScaleInfo;
import cn.devhome.app.utils.PathUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * ScaleMarket
 * 电子秤集合
 * @author lishiying
 * @version 1.0.0
 * @date 2023-12-26 15:26
 **/
@Service
public class ScaleMarket {
    /**
     * 脚本存储路径
     */
    private String scriptPath;
    /**
     * 脚本文件后缀
     */
    private final String fileSuffix = ".groovy";
    /**
     * 所有的电子秤信息
     */
    private List<ScaleInfo> scaleInfoList  = new LinkedList<>();

    public ScaleMarket(@Value("${scales.path:/scales}")String path) {
        this.scriptPath = path;
    }

    @PostConstruct
    public void init() throws Exception{
        // 读取脚本路径中的所有脚本
        String path = PathUtils.getCurrentPath() + scriptPath;
        File fileObject = new File(path);
        File[] listFiles = fileObject.listFiles(filter -> filter.getName().endsWith(fileSuffix));
        for (File file : listFiles) {
            ScaleProxy proxy = new ScaleProxy(file);
            ScaleInfo info = new ScaleInfo();
            info.setName(proxy.getScaleName());
            info.setCode(proxy.getScaleCode());
            info.setProxy(proxy);
            info.setScript(file.getPath());
            scaleInfoList.add(info);
        }

    }

    public List<ScaleInfo> getAllScale() {
        return scaleInfoList;
    }

    /**
     * 根据秤名称查找秤信息
     * @param name 秤名称
     * @return 秤信息
     */
    public ScaleInfo getScaleByName(String name) {
        if (CollectionUtils.isEmpty(scaleInfoList) || StringUtils.isEmpty(name)) {
            return null;
        }
        for (int i = 0; i < scaleInfoList.size(); i++) {
            if (name.equals(scaleInfoList.get(i).getName())){
                return scaleInfoList.get(i);
            }
        }
        return null;
    }
}

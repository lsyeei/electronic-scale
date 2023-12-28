package cn.devhome.app.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.URL;

/**
 * PathUtils
 *
 * @author lishiying
 * @version 1.0.0
 * @date 2023-12-26 09:55
 **/
@Slf4j
public class PathUtils {
    /**
     * 获取路径
     * @return 当前路径
     */
    public static String getCurrentPath(){
        URL resource = PathUtils.class.getResource("/");
        String path = "";
        if (resource == null){
            log.info("路径为空");
            path = System.getProperty("user.dir") + "/";
        }else {
            path = resource.getPath();
            if (path.charAt(0) == '/'){
                path = path.substring(1);
            }
            path = path.replaceAll("/\\./", "/");
        }
        return path;
    }
}

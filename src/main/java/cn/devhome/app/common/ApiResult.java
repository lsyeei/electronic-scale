package cn.devhome.app.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lishiying
 * @version 1.0
 * @date 2020/12/22 17:37
 */
@Slf4j
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
public class ApiResult<T> implements Serializable {
    /**
     * 响应码
     */
    @NotNull
    @JSONField(ordinal = 1)
    private int code;

    /**
     * 响应消息
     */
    @NotNull
    @JSONField(ordinal = 2)
    private String msg;

    /**
     * 响应数据
     */
    @JSONField(ordinal = 3)
    private T data;

    public ApiResult() {

    }
    public static void result(HttpServletResponse response,Integer code,String message) {
        result(response,code,message,null);
    }

    public static <T> void result(HttpServletResponse response,Integer code,String message,T data){
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ApiResult<T> info = new ApiResult();
        info.setCode(code).setMsg(message).setData(data);
        try {
            response.getWriter().println(JSON.toJSONString(info,true));
        }catch (IOException e){
            log.error("HttpServletResponse回写数据错误，错误信息：" + e.getMessage());
        }

    }
    public static ApiResult<Boolean> result(boolean flag){
        if (flag){
            return success();
        }
        return fail();
    }

    public static ApiResult<Boolean> result(ApiCode apiCode){
        return result(apiCode,null);
    }

    public static <T> ApiResult<T> result(ApiCode apiCode,T data){
        return result(apiCode,null,data);
    }

    public static <T> ApiResult<T> result(ApiCode apiCode,String message,T data){
        boolean success = false;
        if (apiCode.getCode() == ApiCode.SUCCESS.getCode()){
            success = true;
        }
        String apiMessage = apiCode.getMessage();
        if (StringUtils.isNotBlank(apiMessage)){
            message = apiMessage;
        }
        return (ApiResult<T>) ApiResult.builder()
                .code(apiCode.getCode())
                .msg(message)
                .data(data)
                .build();
    }

    public static ApiResult<Boolean> success(){
        return success(null);
    }

    public static <T> ApiResult<T> success(T data){
        if(data != null) {
            Class<?> dataClass = data.getClass();
            if (dataClass.equals(Boolean.class)) {
                //布尔型的数据，直接判定
                if (((Boolean) data).booleanValue() == false) {
                    return result(ApiCode.FAIL, data);
//                    return result(ApiCode.FAIL, null);
                }
                //布尔型的数据不进行转换
//                data = null;
            }
        }
        return result(ApiCode.SUCCESS,data);
    }

    /**
     * 数据对象再封装
     * @param key  字段名
     * @param value  字段数据
     * @return ApiResult对象
     */
    public static ApiResult<Map<String,Object>> successMap(String key, Object value){
        Map<String,Object> map = new HashMap<>(1);
        map.put(key,value);
        return success(map);
    }

    public static ApiResult<Boolean> fail() {
        return fail(ApiCode.FAIL);
    }
    public static ApiResult<Boolean> fail(ApiCode apiCode){
        return result(apiCode,null);
    }

    public static ApiResult<String> fail(String message){ return result(ApiCode.FAIL,message,null); }

    public static <T> ApiResult<T> fail(ApiCode apiCode,T data){
        if (ApiCode.SUCCESS == apiCode){
            throw new RuntimeException("失败结果状态码不能为" + ApiCode.SUCCESS.getCode());
        }
        return result(apiCode,data);

    }

    /**
     * 自定义错误信息
     * @param errorCode  错误代码
     * @param message  错误提示信息
     * @return ApiResult对象
     */
    public static  ApiResult<Boolean> fail(Integer errorCode,String message){
        return new ApiResult<Boolean>()
                .setCode(errorCode)
                .setMsg(message);
    }

    /**
     * 错误对象的再封装，将错误信息封装到data字段中
     * @param key 字段名
     * @param value 字段数据
     * @return ApiResult对象
     */
    public static ApiResult<Map<String,Object>> fail(String key,Object value){
        Map<String,Object> map = new HashMap<>(1);
        map.put(key,value);
        return result(ApiCode.FAIL,map);
    }

}

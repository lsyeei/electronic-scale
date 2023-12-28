package cn.devhome.app.controller;

import cn.devhome.app.common.ApiResult;
import cn.devhome.app.domain.ScaleValue;
import cn.devhome.app.view.ModelViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 电子秤 Controller
 * @author lishiying
 * @date 2022/8/3 003
 */
@RequestMapping("/material/weight")
@RestController
public class ScaleController {
    @Autowired(required = false)
    private ModelViewController modelViewController;

    /**
     * 获取秤值
     * @return 秤值
     */
    @GetMapping
    public ApiResult<ScaleValue> getWeightValue(){
        return ApiResult.success(modelViewController.getScale().getScaleValue());
    }
}

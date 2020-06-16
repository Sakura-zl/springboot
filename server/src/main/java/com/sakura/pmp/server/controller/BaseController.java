package com.sakura.pmp.server.controller;

import com.sakura.pmp.common.response.BaseResponse;
import com.sakura.pmp.common.response.StatusCode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author : lzl
 * @date : 11:28 2019/12/2
 */
@Controller
@RequestMapping("/base")
public class BaseController {

    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    /**
     *第一个案例-json格式响应体交互
     * @param name
     * @return
     */
    @GetMapping("/info")
    @ResponseBody
    public BaseResponse info(String name){
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        if(StringUtils.isBlank(name)){
            name = "权限管理平台！";
        }
        baseResponse.setData(name);

        return baseResponse;
    }

    /**
     * 第二个案例：页面跳转-塞值
     * @param name
     * @return
     */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public String page(String name, ModelMap modelMap){
        if (StringUtils.isBlank(name)){
            name="权限管理平台!";
        }
        modelMap.put("name",name);
        modelMap.put("app","权限管理系统!");

        return "pageOne";
    }

}

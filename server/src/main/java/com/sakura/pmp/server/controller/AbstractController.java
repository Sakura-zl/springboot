package com.sakura.pmp.server.controller;

import com.sakura.pmp.model.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * @author : lzl
 * @date : 17:50 2020/2/26
 */
@Controller
public abstract class AbstractController {
    //日记
    private Logger log= LoggerFactory.getLogger(getClass());

    //获取当前登录用户的详情
    protected SysUserEntity getUser(){
        SysUserEntity user= (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

    protected Long getUserId(){
        return getUser().getUserId();
    }
}

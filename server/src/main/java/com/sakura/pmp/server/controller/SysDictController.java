package com.sakura.pmp.server.controller;

import com.google.common.collect.Maps;
import com.sakura.pmp.common.response.BaseResponse;
import com.sakura.pmp.common.response.StatusCode;
import com.sakura.pmp.common.utils.PageUtil;
import com.sakura.pmp.common.utils.ValidatorUtil;
import com.sakura.pmp.model.entity.SysDictEntity;
import com.sakura.pmp.server.service.SysDictService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 字典controller
 * @author : lzl
 * @date : 16:19 2020/5/23
 */
@RestController
@RequestMapping("sys/dict")
public class SysDictController extends AbstractController {

    @Autowired
    private SysDictService sysDictService;

    //列表
    @RequestMapping("/list")
    @RequiresPermissions("sys:dict:list")
    public BaseResponse list(@RequestParam Map<String,Object> params){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            PageUtil page = sysDictService.queryPage(params);

            Map<String,Object> resMap= Maps.newHashMap();
            resMap.put("page", page);
            response.setData(resMap);
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //详情
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:dict:info")
    public BaseResponse info(@PathVariable Long id){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            Map<String,Object> resMap=Maps.newHashMap();
            SysDictEntity entity = sysDictService.getById(id);
            resMap.put("dict", entity);
            response.setData(resMap);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.UpdatePasswordFail);
        }
        return response;
    }

    //新增
    @RequestMapping("/save")
    @RequiresPermissions("sys:dict:save")
    public BaseResponse save(@RequestBody @Validated SysDictEntity dict, BindingResult result){
        String res= ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.Fail.getCode(),res);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            sysDictService.save(dict);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //修改
    @RequestMapping("/update")
    @RequiresPermissions("sys:dict:update")
    public BaseResponse update(@RequestBody @Validated SysDictEntity dict, BindingResult result){
        String res= ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.Fail.getCode(),res);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            sysDictService.updateById(dict);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //删除
    @RequestMapping("/delete")
    @RequiresPermissions("sys:dict:delete")
    public BaseResponse delete(@RequestBody Long[] ids){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            sysDictService.removeByIds(Arrays.asList(ids));
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }
}

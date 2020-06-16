package com.sakura.pmp.server.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sakura.pmp.common.response.BaseResponse;
import com.sakura.pmp.common.response.StatusCode;
import com.sakura.pmp.common.utils.Constant;
import com.sakura.pmp.common.utils.ValidatorUtil;
import com.sakura.pmp.model.entity.SysDeptEntity;
import com.sakura.pmp.server.annotation.LogAnnotation;
import com.sakura.pmp.server.service.SysDeptService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 部门管理controller
 * @author : lzl
 * @date : 15:49 2020/3/7
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController{

    @Autowired
    private SysDeptService sysDeptService;

    //获取一级部门/顶级部门的deptId
    @RequestMapping("/info")
    public BaseResponse info(){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap = Maps.newHashMap();
        Long deptId = 0L;
        try {
            //数据视野决定的顶级部门Id可能不是0
            if(!getUserId().equals(Constant.SUPER_ADMIN)){
                //涉及到数据视野的问题

            }
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        resMap.put("deptId",deptId);
        response.setData(resMap);
        return response;
    }

    //部门列表
    @RequestMapping("/list")
    @RequiresPermissions("sys:dept:list")
    public List<SysDeptEntity> list(){
        return sysDeptService.queryAll(Maps.newHashMap());
    }

    //获取部门树
    @RequestMapping("/select")
    public BaseResponse select(){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap = Maps.newHashMap();
        List<SysDeptEntity> deptList = Lists.newLinkedList();
        try {
            deptList = sysDeptService.queryAll(Maps.newHashMap());

        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        resMap.put("deptList",deptList);
        response.setData(resMap);
        return response;
    }

    //新增
    @LogAnnotation("新增部门")
    @RequestMapping(value = "/save",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("sys:dept:save")
    public BaseResponse save(@RequestBody @Validated SysDeptEntity entity, BindingResult result){
        String res = ValidatorUtil.checkResult(result);
        if(StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.Fail.getCode(),res);
        }

        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            sysDeptService.save(entity);
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //详情
    @RequestMapping("/detail/{deptId}")
    @RequiresPermissions("sys:dept:info")
    public BaseResponse detail(@PathVariable Long deptId){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap = Maps.newHashMap();
        try {
            resMap.put("dept",sysDeptService.getById(deptId));
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }

        response.setData(resMap);
        return response;
    }

    //修改
    @LogAnnotation("修改部门")
    @RequestMapping(value = "/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("sys:dept:update")
    public BaseResponse update(@RequestBody @Validated SysDeptEntity entity, BindingResult result){
        String res = ValidatorUtil.checkResult(result);
        if(StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.Fail.getCode(),res);
        }
        if(entity.getDeptId() == null || entity.getDeptId() <= 0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            sysDeptService.updateById(entity);
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //删除
    @LogAnnotation("删除部门")
    @RequestMapping(value = "/delete")
    @RequiresPermissions("sys:dept:delete")
    public BaseResponse delete(Long deptId){
        if(deptId == null || deptId <= 0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            //如果当前有子部门，需要要求先删除下面的所有子部门，再删除当前部门。
            List<Long> subIds = sysDeptService.queryDeptIds(deptId);
            if(subIds != null && !subIds.isEmpty()){
                return new BaseResponse(StatusCode.DeptHasSubDeptCanNotBeDelete);
            }
            sysDeptService.removeById(deptId);
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }


}

package com.sakura.pmp.server.controller;

import com.google.common.collect.Maps;
import com.sakura.pmp.common.response.BaseResponse;
import com.sakura.pmp.common.response.StatusCode;
import com.sakura.pmp.common.utils.PageUtil;
import com.sakura.pmp.common.utils.ValidatorUtil;
import com.sakura.pmp.model.entity.SysRoleEntity;
import com.sakura.pmp.server.annotation.LogAnnotation;
import com.sakura.pmp.server.service.SysRoleDeptService;
import com.sakura.pmp.server.service.SysRoleMenuService;
import com.sakura.pmp.server.service.SysRoleService;
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
 * 角色controller
 * @author : lzl
 * @date : 19:29 2020/5/16
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysRoleDeptService sysRoleDeptService;

    //分页列表模糊查询
    @RequestMapping("/list")
    @RequiresPermissions("sys:role:list")
    public BaseResponse list(@RequestParam Map<String,Object> paramMap){
        BaseResponse response = new BaseResponse(StatusCode.Success);

        try {
            Map<String,Object> resMap = Maps.newHashMap();

            PageUtil page = sysRoleService.queryPage(paramMap);
            resMap.put("page",page);

            response.setData(resMap);
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //新增
    @LogAnnotation("新增角色")
    @RequestMapping(value = "/save",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("sys:role:save")
    public BaseResponse save(@RequestBody @Validated SysRoleEntity entity, BindingResult result){
        String res = ValidatorUtil.checkResult(result);
        if(StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),res);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            sysRoleService.saveRole(entity);

        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //详情
    @RequestMapping(value = "/info/{id}")
    @RequiresPermissions("sys:role:info")
    public BaseResponse info(@PathVariable Long id){
        if(id == null || id < 0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap = Maps.newHashMap();
        try {
            SysRoleEntity role = sysRoleService.getById(id);

            //获取角色对应的菜单列表
            List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(id);
            role.setMenuIdList(menuIdList);
            //获取角色对应的部门列表
            List<Long> deptIdList = sysRoleDeptService.queryDeptIdList(id);
            role.setDeptIdList(deptIdList);

            resMap.put("role",role);
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(resMap);
        return response;
    }

    //修改
    @LogAnnotation("修改角色")
    @RequestMapping(value = "/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("sys:role:update")
    public BaseResponse update(@RequestBody @Validated SysRoleEntity entity, BindingResult result){
        String res = ValidatorUtil.checkResult(result);
        if(StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),res);
        }

        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            sysRoleService.updateRole(entity);

        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //批量删除
    @LogAnnotation("删除角色")
    @RequestMapping(value = "/delete",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("sys:role:delete")
    public BaseResponse delete(@RequestBody Long[] ids){
        if(ids==null || ids.length<=0){
            BaseResponse response = new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {

            sysRoleService.deleteBatch(ids);
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //获取角色列表
    @RequestMapping("/select")
    public BaseResponse select(){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            Map<String,Object> resMap = Maps.newHashMap();
            resMap.put("list",sysRoleService.list());

            response.setData(resMap);
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

}

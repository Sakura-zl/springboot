package com.sakura.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sakura.pmp.common.utils.PageUtil;
import com.sakura.pmp.model.entity.SysRoleEntity;

import java.util.List;
import java.util.Map;

/**
 * @author : lzl
 * @date : 19:15 2020/5/16
 */
public interface SysRoleService extends IService<SysRoleEntity> {

    PageUtil queryPage(Map<String,Object> map);

    void saveRole(SysRoleEntity role);

    void updateRole(SysRoleEntity entity);

    void deleteBatch(Long[] ids);

}

package com.sakura.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sakura.pmp.model.entity.SysRoleMenuEntity;

import java.util.List;

/**
 * @author : lzl
 * @date : 16:48 2020/5/16
 */
public interface SysRoleMenuService extends IService<SysRoleMenuEntity> {

    void saveOrUpdate(Long roleId, List<Long> menuIdList);

    void deleteBatch(List<Long> roleIds);

    List<Long> queryMenuIdList(Long roleId);
}

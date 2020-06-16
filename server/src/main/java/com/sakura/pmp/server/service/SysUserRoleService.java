package com.sakura.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sakura.pmp.model.entity.SysUserRoleEntity;

import java.util.List;

/**
 * @author : lzl
 * @date : 23:16 2020/5/18
 */
public interface SysUserRoleService extends IService<SysUserRoleEntity> {

    void deleteBatch(List<Long> roleIds);

    void saveOrUpdate(Long userId,List<Long> roleIds);

    List<Long> queryRoleIdList(Long userId);
}

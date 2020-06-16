package com.sakura.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sakura.pmp.model.entity.SysRoleDeptEntity;

import java.util.List;

/**
 * @author : lzl
 * @date : 19:15 2020/5/16
 */
public interface SysRoleDeptService extends IService<SysRoleDeptEntity> {

    void saveOrUpdate(Long roleId, List<Long> deptIdList);

    void deleteBatch(List<Long> roleIds);

    List<Long> queryDeptIdList(Long roleId);
}

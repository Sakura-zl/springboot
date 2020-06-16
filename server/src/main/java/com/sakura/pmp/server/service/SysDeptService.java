package com.sakura.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sakura.pmp.model.entity.SysDeptEntity;

import java.util.List;
import java.util.Map;

/**
 * @author : lzl
 * @date : 15:52 2020/3/7
 */
public interface SysDeptService extends IService<SysDeptEntity> {

    List<SysDeptEntity> queryAll(Map<String,Object> map);

    List<Long> queryDeptIds(Long parentId);

    List<Long> getSubDeptIdList(Long deptId);
}

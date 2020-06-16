package com.sakura.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sakura.pmp.model.entity.SysUserPostEntity;

import java.util.List;

/**
 * @author : lzl
 * @date : 16:32 2020/5/19
 */
public interface SysUserPostService extends IService<SysUserPostEntity> {

    String getPostNameByUserId(Long userId);

    void saveOrUpdate(Long userId, List<Long> postIds);

    List<Long> queryPostIdList(Long userId);
}

package com.sakura.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sakura.pmp.common.utils.PageUtil;
import com.sakura.pmp.model.entity.SysPostEntity;

import java.util.Map;

/**
 * @author : lzl
 * @date : 17:43 2020/2/29
 */
public interface SysPostService extends IService<SysPostEntity> {

    PageUtil queryPage(Map<String,Object> param);

    void savePost(SysPostEntity entity);

    void updatePost(SysPostEntity entity);

    void deletePatch(Long[] ids);
}

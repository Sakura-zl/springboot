package com.sakura.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sakura.pmp.model.entity.SysMenuEntity;

import java.util.List;

/**
 * @author : lzl
 * @date : 23:06 2020/5/15
 */
public interface SysMenuService extends IService<SysMenuEntity> {

    List<SysMenuEntity> queryAll();

    List<SysMenuEntity> queryNotButtonList();

    List<SysMenuEntity> queryByParentId(Long menuId);

    void delete(Long menuId);

    List<SysMenuEntity> getUserMenuList(Long currUserId);
}

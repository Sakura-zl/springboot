package com.sakura.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sakura.pmp.common.utils.PageUtil;
import com.sakura.pmp.model.entity.SysUserEntity;

import java.util.Map;

/**
 * @author : lzl
 * @date : 11:58 2020/2/28
 */
public interface SysUserService extends IService<SysUserEntity> {

    //修改密码
    boolean updatePassword(Long userId,String oldPassword,String newPassword);

    PageUtil queryPage(Map<String,Object> params);

    void saveUser(SysUserEntity entity);

    SysUserEntity getInfo(Long userId);

    void updateUser(SysUserEntity entity);

    void deleteUser(Long[] ids);

    void updatePsd(Long[] ids);
}

package com.sakura.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.sakura.pmp.common.utils.CommonUtil;
import com.sakura.pmp.model.entity.SysUserRoleEntity;
import com.sakura.pmp.model.mapper.SysUserRoleDao;
import com.sakura.pmp.server.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : lzl
 * @date : 23:19 2020/5/18
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRoleEntity> implements SysUserRoleService {

    @Override
    public void deleteBatch(List<Long> roleIds) {
        if(roleIds!=null && !roleIds.isEmpty()){
            String delIds = Joiner.on(",").join(roleIds);
            baseMapper.deleteBatch(CommonUtil.concatStrToInt(delIds,","));
        }

    }

    //维护用户~角色的关联关系
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long userId, List<Long> roleIds) {
        //需要先清除旧的关联信息，再插入新的关联信息
        this.remove(new QueryWrapper<SysUserRoleEntity>().eq("user_id",userId));

        if(roleIds != null && !roleIds.isEmpty()){
            SysUserRoleEntity entity;
            for (Long rId : roleIds) {
                entity = new SysUserRoleEntity();
                entity.setRoleId(rId);
                entity.setUserId(userId);
                this.save(entity);
            }
        }
    }

    //获取分配给用户的角色id列表
    @Override
    public List<Long> queryRoleIdList(Long userId) {
        return baseMapper.queryRoleIdList(userId);
    }
}

package com.sakura.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.sakura.pmp.model.entity.SysUserPostEntity;
import com.sakura.pmp.model.mapper.SysUserPostDao;
import com.sakura.pmp.server.service.SysUserPostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : lzl
 * @date : 16:32 2020/5/19
 */
@Service("sysUserPostService")
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostDao, SysUserPostEntity> implements SysUserPostService {

    //根据用户id获取岗位，如果有多个，用 , 拼接
    @Override
    public String getPostNameByUserId(Long userId) {
        //第一种写法
        /*Set<String> set = baseMapper.getPostNamesByUserId(userId);
        if(set!=null && !set.isEmpty()){
            return Joiner.on(",").join(set);
        }else {
            return "";
        }*/

        String result="";
        List<SysUserPostEntity> list = baseMapper.getByUserId(userId);
        if(list!=null && !list.isEmpty()){
            Set<String> set = list.stream().map(SysUserPostEntity::getPostName).collect(Collectors.toSet());
            return Joiner.on(",").join(set);
        }
        return result;
    }

    //维护用户~岗位的关联关系
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long userId, List<Long> postIds) {
        //需要先清除旧的关联信息，再插入新的关联信息
        this.remove(new QueryWrapper<SysUserPostEntity>().eq("user_id",userId));

        if(postIds != null && !postIds.isEmpty()){
            SysUserPostEntity entity;
            for (Long pId : postIds) {
                entity = new SysUserPostEntity();
                entity.setPostId(pId);
                entity.setUserId(userId);
                this.save(entity);
            }
        }
    }

    //获取分配给用户的岗位id列表
    @Override
    public List<Long> queryPostIdList(Long userId) {
        return baseMapper.queryPostIdList(userId);
    }
}

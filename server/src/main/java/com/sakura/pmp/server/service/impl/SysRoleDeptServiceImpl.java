package com.sakura.pmp.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.sakura.pmp.common.utils.CommonUtil;
import com.sakura.pmp.model.entity.SysRoleDeptEntity;
import com.sakura.pmp.model.mapper.SysRoleDeptDao;
import com.sakura.pmp.server.service.SysRoleDeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author : lzl
 * @date : 19:18 2020/5/16
 */
@Service("sysRoleDeptService")
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptDao, SysRoleDeptEntity> implements SysRoleDeptService {

    private static final Logger log = LoggerFactory.getLogger(SysRoleDeptServiceImpl.class);

    @Autowired
    private SysRoleDeptService sysRoleDeptService;

    //维护角色~部门关系信息
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> deptIdList) {
        //需要先清除旧的关联信息，再插入新的关联信息
        deleteBatch(Arrays.asList(roleId));

        SysRoleDeptEntity entity;
        if(deptIdList != null && !deptIdList.isEmpty()){
            for (Long dId : deptIdList) {
                entity = new SysRoleDeptEntity();
                entity.setRoleId(roleId);
                entity.setDeptId(dId);
                this.save(entity);
            }
        }
    }

    //根据角色id批量删除
    @Override
    public void deleteBatch(List<Long> roleIds) {
        if(roleIds!=null && !roleIds.isEmpty()){
            String delIds = Joiner.on(",").join(roleIds);
            baseMapper.deleteBatch(CommonUtil.concatStrToInt(delIds,","));
        }
    }

    //获取角色对应的部门列表
    @Override
    public List<Long> queryDeptIdList(Long roleId) {
        return baseMapper.queryDeptIdListByRoleId(roleId);
    }
}

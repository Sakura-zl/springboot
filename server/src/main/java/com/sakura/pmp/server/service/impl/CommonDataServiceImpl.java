package com.sakura.pmp.server.service.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.sakura.pmp.common.utils.CommonUtil;
import com.sakura.pmp.common.utils.Constant;
import com.sakura.pmp.model.entity.SysUserEntity;
import com.sakura.pmp.model.mapper.SysDeptDao;
import com.sakura.pmp.model.mapper.SysUserDao;
import com.sakura.pmp.server.service.CommonDataService;
import com.sakura.pmp.server.service.SysDeptService;
import com.sakura.pmp.server.shiro.ShiroUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 通用化的部门数据权限控制service
 * @author : lzl
 * @date : 15:19 2020/5/22
 */
@Service
public class CommonDataServiceImpl implements CommonDataService {

    private static final Logger log= LoggerFactory.getLogger(CommonDataServiceImpl.class);

    @Autowired
    private SysDeptDao sysDeptDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysDeptService sysDeptService;

    //获取当前登录用户的部门数据Id列表
    @Override
    public Set<Long> getCurrUserDataDeptIds() {
        Set<Long> dataIds= Sets.newHashSet();

        SysUserEntity currUser= ShiroUtil.getUserEntity();
        if (Constant.SUPER_ADMIN.equals(currUser.getUserId())){
            dataIds=sysDeptDao.queryAllDeptIds();

        }else{
            //分配给用户的部门数据权限id列表
            Set<Long> userDeptDataIds=sysUserDao.queryDeptIdsByUserId(currUser.getUserId());
            if (userDeptDataIds!=null && !userDeptDataIds.isEmpty()){
                dataIds.addAll(userDeptDataIds);
            }

            //用户所在的部门及其子部门Id列表 ~ 递归实现
            dataIds.add(currUser.getDeptId());

            List<Long> subDeptIdList=sysDeptService.getSubDeptIdList(currUser.getDeptId());
            dataIds.addAll(Sets.newHashSet(subDeptIdList));
        }
        return dataIds;
    }

    //将 部门数据Id列表 转化为 id拼接 的字符串
    @Override
    public String getCurrUserDataDeptIdsStr() {
        String result=null;

        Set<Long> dataSet=this.getCurrUserDataDeptIds();
        if (dataSet!=null && !dataSet.isEmpty()){
            result= CommonUtil.concatStrToInt(Joiner.on(",").join(dataSet),",");
        }

        return result;
    }

}

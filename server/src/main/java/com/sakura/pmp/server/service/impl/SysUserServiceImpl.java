package com.sakura.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sakura.pmp.common.utils.Constant;
import com.sakura.pmp.common.utils.PageUtil;
import com.sakura.pmp.common.utils.QueryUtil;
import com.sakura.pmp.model.entity.SysDeptEntity;
import com.sakura.pmp.model.entity.SysUserEntity;
import com.sakura.pmp.model.entity.SysUserPostEntity;
import com.sakura.pmp.model.entity.SysUserRoleEntity;
import com.sakura.pmp.model.mapper.SysUserDao;
import com.sakura.pmp.server.service.SysDeptService;
import com.sakura.pmp.server.service.SysUserPostService;
import com.sakura.pmp.server.service.SysUserRoleService;
import com.sakura.pmp.server.service.SysUserService;
import com.sakura.pmp.server.shiro.ShiroUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author : lzl
 * @date : 12:02 2020/2/28
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysUserPostService sysUserPostService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    //更新密码 ~ 借助于mybatis-plus的方法来实现
    @Override
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        SysUserEntity entity = new SysUserEntity();
        entity.setPassword(newPassword);
        Boolean res=this.update(entity, new QueryWrapper<SysUserEntity>().eq("user_id",userId).eq("password",oldPassword));
        return res;
    }

    //分页列表模糊查询
    @Override
    public PageUtil queryPage(Map<String, Object> params) {

        String search= (params.get("username") == null)? "": params.get("username").toString();

        //调用自封装的分页查询工具
        IPage<SysUserEntity> queryPage = new QueryUtil<SysUserEntity>().getQueryPage(params);

        QueryWrapper wrapper = new QueryWrapper<SysUserEntity>()
                .like(StringUtils.isNotBlank(search),"username",search.trim())
                .or(StringUtils.isNotBlank(search))
                .like(StringUtils.isNotBlank(search),"name",search.trim());

        IPage<SysUserEntity> resPage = this.page(queryPage,wrapper);

        SysDeptEntity dept;
        for (SysUserEntity user:resPage.getRecords()) {
            try {
                dept = sysDeptService.getById(user.getDeptId());
                user.setDeptName(dept!=null && StringUtils.isNotBlank(dept.getName()) ? dept.getName() : "");
                String postName = sysUserPostService.getPostNameByUserId(user.getUserId());
                user.setPostName(postName);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return new PageUtil(resPage);
    }

    //保存用户数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SysUserEntity entity) {
        if(this.getOne(new QueryWrapper<SysUserEntity>().eq("username",entity.getUsername()))!=null){
            throw  new RuntimeException("用户名已存在!");
        }
        entity.setCreateTime(new Date());

        //加密密码串
        String salt = RandomStringUtils.randomAlphanumeric(20);
        String password = ShiroUtil.sha256(entity.getPassword(),salt);
        entity.setPassword(password);
        entity.setSalt(salt);
        this.save(entity);

        //维护用户~角色的关联关系
        sysUserRoleService.saveOrUpdate(entity.getUserId(),entity.getRoleIdList());

        //维护用户~岗位的关联关系
        sysUserPostService.saveOrUpdate(entity.getUserId(),entity.getPostIdList());
    }

    //获取用户信息，包括其分配的角色、岗位关联信息
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUserEntity getInfo(Long userId) {
        SysUserEntity entity = this.getById(userId);

        //获取用户分配的角色关联信息
        List<Long> roleIds = sysUserRoleService.queryRoleIdList(userId);
        entity.setRoleIdList(roleIds);
        //获取用户分配的岗位关联信息
        List<Long> postIds = sysUserPostService.queryPostIdList(userId);
        entity.setPostIdList(postIds);
        return entity;
    }


    //更新用户信息
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUserEntity entity) {
        SysUserEntity oldUser = this.getById(entity.getUserId());
        if(oldUser==null){
            return;
        }
        if(!oldUser.getUsername().equals(entity.getUsername())){
            if(this.getOne(new QueryWrapper<SysUserEntity>().eq("username",entity.getUsername()))!=null){
                throw  new RuntimeException("用户名已存在!");
            }
        }

        if(StringUtils.isNotBlank(entity.getPassword())){
            String password = ShiroUtil.sha256(entity.getPassword(),oldUser.getSalt());
            entity.setPassword(password);
        }
        this.updateById(entity);

        //维护用户~角色的关联关系
        sysUserRoleService.saveOrUpdate(entity.getUserId(),entity.getRoleIdList());

        //维护用户~岗位的关联关系
        sysUserPostService.saveOrUpdate(entity.getUserId(),entity.getPostIdList());
    }

    //删除用户, 删除用户信息，还需要删除 用户~角色 、用户~岗位 关联关系信息
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long[] ids) {
        if(ids!=null && ids.length>0){
            List<Long> userIds = Arrays.asList(ids);
            this.removeByIds(userIds);
            /*for (Long uId:userIds){
                sysUserRoleService.remove(new QueryWrapper<SysUserRoleEntity>().eq("user_id",uId));
                sysUserPostService.remove(new QueryWrapper<SysUserPostEntity>().eq("user_id",uId));
            }*/

            //java8写法
            userIds.stream().forEach(uId -> {
                sysUserRoleService.remove(new QueryWrapper<SysUserRoleEntity>().eq("user_id",uId));
                sysUserPostService.remove(new QueryWrapper<SysUserPostEntity>().eq("user_id",uId));
            });
        }
    }

    //重置密码
    @Override
    public void updatePsd(Long[] ids) {
        if(ids!=null && ids.length>0){
            SysUserEntity entity;
            for (Long uId:ids){
                entity = new SysUserEntity();
                String salt = RandomStringUtils.randomAlphanumeric(20);
                String newPassword = ShiroUtil.sha256(Constant.DefaultPassword,salt);
                entity.setPassword(newPassword);
                entity.setSalt(salt);
                this.update(entity, new QueryWrapper<SysUserEntity>().eq("user_id",uId));
            }
        }
    }
}

package com.sakura.pmp.server.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sakura.pmp.common.utils.Constant;
import com.sakura.pmp.model.entity.SysMenuEntity;
import com.sakura.pmp.model.entity.SysUserEntity;
import com.sakura.pmp.model.mapper.SysUserDao;
import com.sakura.pmp.server.service.SysMenuService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * shiro用于认证用户~授权
 * @author : lzl
 * @date : 20:03 2020/2/26
 */
@Component
public class UserRealm extends AuthorizingRealm {

    private static final Logger log= LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 资源-权限分配 ~ 授权 ~ 需要将分配给当前用户的权限列表塞给shiro的权限字段中去
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取当前登录用户(主体)
        SysUserEntity user = (SysUserEntity) principalCollection.getPrimaryPrincipal();
        Long userId = user.getUserId();
        List<String> perms = Lists.newLinkedList();

        //超级系统管理员拥有最高的权限，不需要发出sql的查询，直接拥有全部权限；否则需要根据当前用户id查询所有权限列表
        if(userId.equals(Constant.SUPER_ADMIN)){
            List<SysMenuEntity> list = sysMenuService.list();
            if(list!=null && !list.isEmpty()){
                perms = list.stream().map(SysMenuEntity::getPerms).collect(Collectors.toList());
            }
        }else {
            perms = sysUserDao.queryAllPerms(userId);
        }

        //对于每一个授权编码进行 , 的解析拆分
        Set<String> stringPermissions = Sets.newHashSet();
        if(perms!=null && !perms.isEmpty()){
            for(String p:perms){
                if(StringUtils.isNotBlank(p)){
                    stringPermissions.addAll(Arrays.asList(StringUtils.split(p.trim(),",")));
                }
            }
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(stringPermissions);
        return info;
    }

    /**
     * 用户认证 ~ 登录认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token= (UsernamePasswordToken) authenticationToken;
        final String userName=token.getUsername();
        final String password=String.valueOf(token.getPassword());

        log.info("用户名: {} 密码：{}",userName,password);

//        SysUserEntity entity=sysUserDao.selectByUserName(userName);
        SysUserEntity entity=sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("username",userName));
        //账户不存在
        if (entity==null){
            throw new UnknownAccountException("账户不存在!");
        }
        //账户被禁用
        if (0 == entity.getStatus()){
            throw new DisabledAccountException("账户已被禁用,请联系管理员!");
        }
        //账户名密码不匹配
//        if (!entity.getPassword().equals(password)){
//            throw new IncorrectCredentialsException("账户密码不匹配!");
//        }
//        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(entity, entity.getPassword(),getName());

        //第二种验证逻辑-交给shiro的密钥匹配器去实现
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(entity, entity.getPassword(), ByteSource.Util.bytes(entity.getSalt()), getName());
        return info;
    }

    /**
     * 密码验证器~匹配逻辑 ~ 第二种验证逻辑
     * @param credentialsMatcher
     */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
        shaCredentialsMatcher.setHashAlgorithmName(ShiroUtil.hashAlgorithmName);
        shaCredentialsMatcher.setHashIterations(ShiroUtil.hashIterations);
        super.setCredentialsMatcher(shaCredentialsMatcher);
    }
}

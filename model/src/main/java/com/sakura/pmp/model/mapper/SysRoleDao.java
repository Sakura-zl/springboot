package com.sakura.pmp.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sakura.pmp.model.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

//角色管理
@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {
	

}

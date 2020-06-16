package com.sakura.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sakura.pmp.common.utils.PageUtil;
import com.sakura.pmp.common.utils.QueryUtil;
import com.sakura.pmp.model.entity.SysLogEntity;
import com.sakura.pmp.model.mapper.SysLogDao;
import com.sakura.pmp.server.service.SysLogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author : lzl
 * @date : 15:37 2020/5/23
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogEntity> implements SysLogService {

    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        String key = (String)params.get("key");

        IPage queryPage=new QueryUtil<SysLogEntity>().getQueryPage(params);

        QueryWrapper<SysLogEntity> wrapper=new QueryWrapper<SysLogEntity>()
                .like(StringUtils.isNotBlank(key),"username", key)
                .or(StringUtils.isNotBlank(key))
                .like(StringUtils.isNotBlank(key),"operation", key);
        IPage<SysLogEntity> page=this.page(queryPage,wrapper);

        return new PageUtil(page);
    }

    @Override
    public void truncate() {
        baseMapper.truncate();
    }
}

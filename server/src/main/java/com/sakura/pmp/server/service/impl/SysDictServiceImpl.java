package com.sakura.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sakura.pmp.common.utils.PageUtil;
import com.sakura.pmp.common.utils.QueryUtil;
import com.sakura.pmp.model.entity.SysDictEntity;
import com.sakura.pmp.model.mapper.SysDictDao;
import com.sakura.pmp.server.service.SysDictService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author : lzl
 * @date : 16:26 2020/5/23
 */
@Service("sysDictService")
public class SysDictServiceImpl extends ServiceImpl<SysDictDao, SysDictEntity> implements SysDictService {

    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        String name = (String)params.get("name");

        IPage queryPage=new QueryUtil<SysDictEntity>().getQueryPage(params);

        //查询包装器
        QueryWrapper<SysDictEntity> wrapper=new QueryWrapper<SysDictEntity>()
                .like(StringUtils.isNotBlank(name),"name", name);
        IPage<SysDictEntity> page=this.page(queryPage,wrapper);

        return new PageUtil(page);
    }

}

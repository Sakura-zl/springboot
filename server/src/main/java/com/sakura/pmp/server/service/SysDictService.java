package com.sakura.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sakura.pmp.common.utils.PageUtil;
import com.sakura.pmp.model.entity.SysDictEntity;

import java.util.Map;

/**
 * @author : lzl
 * @date : 16:26 2020/5/23
 */
//数据字典
public interface SysDictService extends IService<SysDictEntity> {

    PageUtil queryPage(Map<String, Object> params);
}

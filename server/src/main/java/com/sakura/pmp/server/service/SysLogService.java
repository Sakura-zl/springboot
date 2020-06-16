package com.sakura.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sakura.pmp.common.utils.PageUtil;
import com.sakura.pmp.model.entity.SysLogEntity;

import java.util.Map;

/**
 * @author : lzl
 * @date : 15:36 2020/5/23
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtil queryPage(Map<String, Object> params);

    void truncate();

}

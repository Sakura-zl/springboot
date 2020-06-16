package com.sakura.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sakura.pmp.common.utils.PageUtil;
import com.sakura.pmp.model.entity.AttendRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * @author : lzl
 * @date : 16:53 2020/6/6
 */
public interface AttendRecordService extends IService<AttendRecordEntity> {

    PageUtil queryPage(Map<String, Object> params);

    List<AttendRecordEntity> selectAll(Map<String, Object> params);

    List<Map<Integer, Object>> manageExport(List<AttendRecordEntity> list);
}

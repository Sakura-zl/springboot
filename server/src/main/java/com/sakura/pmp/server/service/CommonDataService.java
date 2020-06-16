package com.sakura.pmp.server.service;

import java.util.Set;

/**
 * @author : lzl
 * @date : 15:18 2020/5/22
 */
public interface CommonDataService {

    Set<Long> getCurrUserDataDeptIds();

    String getCurrUserDataDeptIdsStr();
}

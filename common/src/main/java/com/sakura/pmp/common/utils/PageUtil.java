package com.sakura.pmp.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author : lzl
 * @date : 16:09 2020/3/1
 */
@Data
public class PageUtil implements Serializable {


    //总记录数
    private long totalCount;

    //每页显示记录数
    private long pageSize;

    //总页数
    private long totalPage;

    //当前页数
    private long currPage;

    //数据列表
    private List<?> list;

    public PageUtil(long totalCount, long pageSize, long totalPage, long currPage, List<?> list) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.currPage = currPage;
        this.list = list;
    }

    public PageUtil(IPage<?> page){
        this.totalCount = page.getTotal();
        this.pageSize = page.getSize();
        this.totalPage = page.getPages();
        this.currPage = page.getCurrent();
        this.list = page.getRecords();
    }
}

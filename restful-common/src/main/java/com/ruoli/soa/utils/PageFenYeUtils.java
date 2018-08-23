package com.ruoli.soa.utils;

import com.github.pagehelper.PageHelper;
import com.ruoli.soa.model.Datagrid;
import com.ruoli.soa.model.PageInfo;

import java.util.List;

/**
 * @author hzr/18060832
 * @date 2018/8/20 17:45
 */
public class PageFenYeUtils<T>
{
    private int pageSize;
    private int pageCurr;

    public Datagrid pageFenYeHandle(List<T> list)
    {
        PageInfo<T> pageInfo = new PageInfo<>(list);
        Datagrid datagrid = new Datagrid(pageInfo.getTotal(), pageInfo.getList());

        return datagrid;
    }

}

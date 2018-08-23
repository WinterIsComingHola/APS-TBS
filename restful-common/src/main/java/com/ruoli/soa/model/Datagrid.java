package com.ruoli.soa.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Datagrid传数据用的类，包括total（总数）和rows（数据）
 * @author hzr/18060832
 * @date 2018/8/20 11:07
 */
public class Datagrid<T>
{
    private long total;
    private List<T> list = new ArrayList<>();
    public Datagrid(){
        super();
    }
    public Datagrid(long total, List<T> list){
        super();
        this.total = total;
        this.list = list;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public List<T> getList()
    {
        return list;
    }

    public void setList(List<T> list)
    {
        this.list = list;
    }
}

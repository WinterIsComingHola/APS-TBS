package com.roli.apsimock.model.api;


import java.util.List;

/**
 * @author hzr/18060832
 * @date 2018/8/29 15:48
 */
public class MockRunResultForAjax
{

    private int code;
    private String msg;
    private int count;
    private List<MockRunResultDetail> data;

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public List<MockRunResultDetail> getData()
    {
        return data;
    }

    public void setData(List<MockRunResultDetail> data)
    {
        this.data = data;
    }
}

package com.roli.apsimock.model;

import com.ruoli.soa.model.SoaRestParam;

/**
 * @author xuxinyu
 * @date 2018/3/6 下午8:23
 */
public class ApsSoaParam extends SoaRestParam {

    private String businessParam;

    public String getBusinessParam() {
        return businessParam;
    }

    public void setBusinessParam(String businessParam) {
        this.businessParam = businessParam;
    }
}

package com.ruoli.soa.model;

import java.io.Serializable;

public class SoaHttpRespResult implements Serializable {


    private static final long serialVersionUID = 5829956619925624981L;
    private String respHeader;
    private String respBody;

    public String getRespHeader() {
        return respHeader;
    }

    public void setRespHeader(String respHeader) {
        this.respHeader = respHeader;
    }

    public String getRespBody() {
        return respBody;
    }

    public void setRespBody(String respBody) {
        this.respBody = respBody;
    }
}

package com.roli.apsimock.model.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NoticeForAjaxDetail implements Serializable {


    private static final long serialVersionUID = -6129596516911925560L;
    private Integer zizeng;
    private String info;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;


    public Integer getZizeng() {
        return zizeng;
    }

    public void setZizeng(Integer zizeng) {
        this.zizeng = zizeng;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}

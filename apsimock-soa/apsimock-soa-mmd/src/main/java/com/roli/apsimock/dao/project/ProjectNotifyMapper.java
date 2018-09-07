package com.roli.apsimock.dao.project;

import com.roli.apsimock.model.user.NoticeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProjectNotifyMapper {

    //新增一个通知消息入库
    public void addnotification(@Param("userAccounts")List<String> userAccounts,
                                @Param("notifyContent")String notifyContent);

    public List<NoticeRecord> queryNoticeRecordByuserAccount(@Param("userAccount")String userAccount);

}

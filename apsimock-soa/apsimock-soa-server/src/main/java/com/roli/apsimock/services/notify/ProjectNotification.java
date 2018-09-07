package com.roli.apsimock.services.notify;

import com.roli.apsimock.model.user.NoticeRecord;
import com.roli.common.exception.BusinessException;

import java.util.List;

public interface ProjectNotification {
    public void addProjectNtify(List<String> userAccounts, String notifyContent) throws BusinessException;

    public List<NoticeRecord> queryNoticeRecordByUser(String userAccount) throws BusinessException;
}

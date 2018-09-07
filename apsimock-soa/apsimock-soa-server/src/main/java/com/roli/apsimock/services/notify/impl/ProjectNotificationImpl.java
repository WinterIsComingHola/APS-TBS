package com.roli.apsimock.services.notify.impl;

import com.roli.apsimock.dao.project.ProjectNotifyMapper;
import com.roli.apsimock.model.user.NoticeRecord;
import com.roli.apsimock.services.notify.ProjectNotification;
import com.roli.common.exception.BusinessException;
import com.roli.common.exception.CustomAssert;
import com.roli.common.model.enums.ErrorsEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProjectNotificationImpl implements ProjectNotification {

    @Resource
    ProjectNotifyMapper projectNotifyMapper;

    @Override
    @Transactional
    public void addProjectNtify(List<String> userAccounts, String notifyContent) throws BusinessException {
        CustomAssert.isNotNull(userAccounts, ErrorsEnum.OBJECT_NULL);
        CustomAssert.isNotNull(notifyContent, ErrorsEnum.OBJECT_NULL);
        projectNotifyMapper.addnotification(userAccounts,notifyContent);
    }

    @Override
    public List<NoticeRecord> queryNoticeRecordByUser(String userAccount) throws BusinessException {

        CustomAssert.isNotNull(userAccount, ErrorsEnum.OBJECT_NULL);

        List<NoticeRecord> noticeRecords = projectNotifyMapper.queryNoticeRecordByuserAccount(userAccount);

        if(noticeRecords == null || noticeRecords.size()==0){
            BusinessException.throwMessage(ErrorsEnum.RETURN_NULL);
        }
        return noticeRecords;

    }
}

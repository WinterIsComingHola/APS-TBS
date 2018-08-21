package com.roli.apsimock.dao.api;

import com.roli.apsimock.model.api.FieldInfo;
import com.roli.apsimock.model.api.FieldInfoOV;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/4/3 下午7:56
 */
public interface FieldInfoMapper {

    public void addField(FieldInfoOV fieldInfoOV);
    public List<FieldInfo> queryFieldByName(@Param("fieldName")String fieldName
                                    , @Param("apiName")String apiName);

    public FieldInfo queryFatherFieldByFid(@Param("fatherId")Integer fatherId);

    //根据接口的路径查询所有的字段信息
    public List<FieldInfo> queryAllFieldByUrlPath(@Param("urlPath")String urlPath);
}

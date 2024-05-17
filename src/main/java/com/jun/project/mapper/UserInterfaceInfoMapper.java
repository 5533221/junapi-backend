package com.jun.project.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jun.project.common.BaseResponse;
import com.jun.project.model.vo.InterfaceVO;
import com.junapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author 27164
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2024-05-09 21:32:09
* @Entity generator.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    //分析功能  统计接口的调用次数

    List<UserInterfaceInfo> anysisInvokeInterfaceTop(int limit);

}





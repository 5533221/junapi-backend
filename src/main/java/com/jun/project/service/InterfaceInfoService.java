package com.jun.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.junapicommon.model.entity.InterfaceInfo;


/**
* @author 27164
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-05-03 21:02:10
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo post, boolean add);

}

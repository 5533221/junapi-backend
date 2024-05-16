package com.jun.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.junapicommon.model.entity.UserInterfaceInfo;

public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean b);

    //当用户调用了 接口  调用接口次数+1 总次数-1
    boolean invokeAddCount(long interfaceInfoId, long userId);
}

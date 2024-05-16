package com.jun.project.service.impl.inner;

import com.jun.project.service.UserInterfaceInfoService;
import com.junapicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 27164
 * @version 1.0
 * @description: TODO
 * @date 2024/5/16 17:23
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    //调用次数+1

    @Autowired
    private UserInterfaceInfoService userInterfaceInfoServicel;

    @Override
    public boolean invokeAddCount(long interfaceInfoId, long userId) {

        //调用注入的UserInterfaceInfoService 的 invokeAddCount() 方法
        return  userInterfaceInfoServicel.invokeAddCount(interfaceInfoId,userId);
    }
}

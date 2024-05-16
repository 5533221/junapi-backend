package com.jun.project.service.impl;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jun.project.exception.BusinessException;
import com.jun.project.mapper.UserInterfaceInfoMapper;
import com.jun.project.common.ErrorCode;
import com.jun.project.service.UserInterfaceInfoService;
import com.junapicommon.model.entity.UserInterfaceInfo;
import org.springframework.stereotype.Service;

/**
* @author 27164
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2024-05-09 21:32:09
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo> implements UserInterfaceInfoService {

    @Override
    public void validInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean b) {

        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = userInterfaceInfo.getUserId();
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();

        // 创建时，所有参数必须非空
        if (b) {
            if (userId<= 0 || interfaceInfoId <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口或用户不存在");
            }
        }
        if(userInterfaceInfo.getLeftNum() <0){

            throw new BusinessException(ErrorCode.PARAMS_ERROR,"剩余次数不能小于0");
        }

    }

    //当用户调用了 接口  调用接口次数+1 总次数-1
    @Override
    public boolean invokeAddCount(long interfaceInfoId,long userId) {

        UpdateWrapper<UserInterfaceInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("status",1);
        wrapper.eq("interfaceInfoId",interfaceInfoId);
        wrapper.eq("userId",userId);
        wrapper.setSql("totalNum = totalNum+1,leftNum = leftNum - 1");

       return  update(wrapper);

    }
}





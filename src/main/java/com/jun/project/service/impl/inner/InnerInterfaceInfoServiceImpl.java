package com.jun.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jun.project.common.ErrorCode;
import com.jun.project.exception.BusinessException;
import com.jun.project.mapper.InterfaceInfoMapper;
import com.junapicommon.model.entity.InterfaceInfo;
import com.junapicommon.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 27164
 * @version 1.0
 * @description: TODO
 * @date 2024/5/16 17:21
 */

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Autowired
    private InterfaceInfoMapper interfaceInfoMapper;

    //通过url 和 method 获取接口信息
    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {

        if(StringUtils.isAnyBlank(url,method)){

            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"参数不能为空");
        }

        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("url",url);
        wrapper.eq("method",method);

        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(wrapper);

        return interfaceInfo;
    }
}

package com.jun.project.service.impl;
import java.util.Date;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jun.project.exception.BusinessException;
import com.jun.project.common.ErrorCode;
import com.jun.project.mapper.InterfaceInfoMapper;
import com.jun.project.service.InterfaceInfoService;
import com.junapicommon.model.entity.InterfaceInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author 27164
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2024-05-03 21:02:10
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{

    //校验是否合格的参数
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceinfo, boolean add) {

     Long id = interfaceinfo.getId();
     String name = interfaceinfo.getName();
     String description = interfaceinfo.getDescription();
     String url = interfaceinfo.getUrl();
     String requestHeader = interfaceinfo.getRequestHeader();
     String responseHeader = interfaceinfo.getResponseHeader();
     Integer status = interfaceinfo.getStatus();
     String method = interfaceinfo.getMethod();
     Long userId = interfaceinfo.getUserId();
     Date createTime = interfaceinfo.getCreateTime();
     Date updateTime = interfaceinfo.getUpdateTime();
     Integer isDelete = interfaceinfo.getIsDelete();

        if (interfaceinfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name,description,url,requestHeader,responseHeader,method)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }

    }
    

}





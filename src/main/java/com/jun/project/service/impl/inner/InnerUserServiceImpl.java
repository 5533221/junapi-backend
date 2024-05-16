package com.jun.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jun.project.common.ErrorCode;
import com.jun.project.exception.BusinessException;
import com.jun.project.mapper.UserMapper;
import com.junapicommon.model.entity.User;
import com.junapicommon.service.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 27164
 * @version 1.0
 * @description: TODO
 * @date 2024/5/16 17:24
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     *
     *
     * 通过accesskey查询用户
     *
     * */
    @Override
    public User getInvokeUser(String accessKey) {

        if(StringUtils.isAnyBlank(accessKey)){

            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"参数不能为空");
        }

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("accessKey",accessKey);

        User user = userMapper.selectOne(wrapper);

        return user;
    }

}

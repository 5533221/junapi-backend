package com.jun.project.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jun.project.common.BaseResponse;
import com.jun.project.common.ResultUtils;
import com.jun.project.mapper.UserInterfaceInfoMapper;
import com.jun.project.model.vo.InterfaceVO;
import com.jun.project.service.InterfaceInfoService;
import com.jun.project.service.UserInterfaceInfoService;
import com.junapicommon.model.entity.InterfaceInfo;
import com.junapicommon.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 27164
 * @version 1.0
 * @description: TODO
 * @date 2024/5/17 9:26
 */

@RestController
@RequestMapping("/Analysis")
@Slf4j
public class AnalysisController {

    @Autowired
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Autowired
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/invoke/top")
    public BaseResponse<List<InterfaceVO>> AnalysisInvokeInterface(){
        //查询前三的接口
        List<UserInterfaceInfo> userInterfaceInfos = userInterfaceInfoMapper.anysisInvokeInterfaceTop(3);


        //分组  获取到Interfaceid
        Map<Long, List<UserInterfaceInfo>> map = userInterfaceInfos.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));

        System.out.println("-------------------"+map);

        //key为InterfaceInfoId 根据InterfaceInfoId查询接口信息
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.in("id",map.keySet());
        //查询对应的接口信息
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(wrapper);


        //转为vo 响应给前端
        List<InterfaceVO> interfaceVOList = interfaceInfoList.stream().map(InterfaceInfo -> {

            // 创建一个新的接口信息VO对象
            InterfaceVO interfaceVO = new InterfaceVO();
            //将属性拷贝给VO
            BeanUtil.copyProperties(InterfaceInfo, interfaceVO);
            //从map中获取到total
            Integer totalNum = map.get(InterfaceInfo.getId()).get(0).getTotalNum();

            interfaceVO.setTotalNum(totalNum);


            return interfaceVO;

        }).collect(Collectors.toList());

        return ResultUtils.success(interfaceVOList);
    }



}

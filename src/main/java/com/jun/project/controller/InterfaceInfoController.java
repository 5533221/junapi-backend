package com.jun.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.jun.project.annotation.AuthCheck;
import com.jun.project.common.*;
import com.jun.project.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.jun.project.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.jun.project.service.InterfaceInfoService;
import com.junapicommon.model.entity.InterfaceInfo;
import com.junapicommon.model.entity.User;
import com.sdk.client.JunClient;
import com.jun.project.constant.CommonConstant;
import com.jun.project.exception.BusinessException;
import com.jun.project.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.jun.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.jun.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.jun.project.model.enums.InterfaceStatusEnum.OFFLINE;
import static com.jun.project.model.enums.InterfaceStatusEnum.ONLINE;


/**
 * 帖子接口
 *
 * @author yupi
 */
@RestController
@RequestMapping("/InterfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Autowired
    private InterfaceInfoService InterfaceInfoService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param InterfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest InterfaceInfoAddRequest, HttpServletRequest request) {
        if (InterfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo InterfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(InterfaceInfoAddRequest, InterfaceInfo);
        // 校验
        InterfaceInfoService.validInterfaceInfo(InterfaceInfo, true);

        User loginUser = userService.getLoginUser(request);
        InterfaceInfo.setUserId(loginUser.getId());
        boolean result = InterfaceInfoService.save(InterfaceInfo);

        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId = InterfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = InterfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = InterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * todo 可能改坏了
     * @param InterfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest InterfaceInfoUpdateRequest,
                                            HttpServletRequest request) {
        if (InterfaceInfoUpdateRequest == null || InterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(InterfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        InterfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = InterfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = InterfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = InterfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo InterfaceInfo = InterfaceInfoService.getById(id);
        return ResultUtils.success(InterfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param InterfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest InterfaceInfoQueryRequest) {
        InterfaceInfo InterfaceInfoQuery = new InterfaceInfo();
        if (InterfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(InterfaceInfoQueryRequest, InterfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(InterfaceInfoQuery);
        List<InterfaceInfo> InterfaceInfoList = InterfaceInfoService.list(queryWrapper);
        return ResultUtils.success(InterfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param InterfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest InterfaceInfoQueryRequest, HttpServletRequest request) {
        if (InterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo InterfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(InterfaceInfoQueryRequest, InterfaceInfoQuery);
        long current = InterfaceInfoQueryRequest.getCurrent();
        long size = InterfaceInfoQueryRequest.getPageSize();
        String sortField = InterfaceInfoQueryRequest.getSortField();
        String sortOrder = InterfaceInfoQueryRequest.getSortOrder();

        String content = InterfaceInfoQuery.getDescription();
        // content 需支持模糊搜索
        InterfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(InterfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> InterfaceInfoPage = InterfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(InterfaceInfoPage);
    }





    @Resource
    private JunClient client;


    /**
     * 发布接口  将接口的状态改为 1  表示开启  0表示下线
     *  只需要传递id  就可以修改 接口  需要设置一个单独的 id字段
     *
     * @param idRequest 前端传来id的对象
     * @param request
     * @return
     * Todo  这里的interfaceInfo 使用的是数据库查询出的  如果行不通 就直接new  下面下线接口也是一样的意思
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> OnlineInterfaceInfo(@RequestBody IdRequest idRequest,
                                                     HttpServletRequest request) {

        //1.校验参数 是否为空  为空抛出异常
        Long id = idRequest.getId();
        if(id == null || id <= 0){
            //参数错误
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        }
        //2.校验 接口是否存在 数据库查询
        InterfaceInfo interfaceInfo = InterfaceInfoService.getById(id);

        if(interfaceInfo == null){
            //请求数据不存在
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);

        }
        //3.判断是否可以调用  接口
        com.sdk.modal.User user = new com.sdk.modal.User();
        user.setUserName("测试用户！！！");

        String res = null;
        try {
            res = client.GetNameByPostAndBody(user);
        } catch (Exception e) {

            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口错误,暂不能使用！！");
        }

        if(res.isEmpty()){

            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口错误,暂不能使用！！");
        }
        //4.修改接口状态为 1
        //仅管理员才可以修改
        interfaceInfo.setStatus(ONLINE.getValue());
        boolean isSuccess = InterfaceInfoService.updateById(interfaceInfo);

        return ResultUtils.success(isSuccess);
    }


    /**
     * 下线接口  将接口的状态改为 0  1表示开启  0表示下线
     *  只需要传递id  就可以修改 接口  需要设置一个单独的 id字段
     *
     * @param idRequest 前端传来id的对象
     * @param request
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> OfflineInterfaceInfo(@RequestBody IdRequest idRequest,
                                                     HttpServletRequest request) {

        //1.校验参数 是否为空  为空抛出异常
        Long id = idRequest.getId();
        if(id == null || id <= 0){
            //参数错误
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        }
        //2.校验 接口是否存在 数据库查询
        InterfaceInfo interfaceInfo = InterfaceInfoService.getById(id);

        if(interfaceInfo == null){
            //请求数据不存在
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);

        }
        //4.修改接口状态为 0
        //仅管理员才可以修改
        interfaceInfo.setStatus(OFFLINE.getValue());
        boolean isSuccess = InterfaceInfoService.updateById(interfaceInfo);

        return ResultUtils.success(isSuccess);
    }


    /**
     * 测试调用接口
     *
     * */
    @PostMapping("/invoke")
    public BaseResponse<Object> TestInvokeInterfaceInfo(
            @RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
                                                     HttpServletRequest request) {

        //1.校验参数 是否为空  为空抛出异常
        Long id = interfaceInfoInvokeRequest.getId();
        if(id == null || id <= 0){
            //参数错误
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        }
        //2.获取前端传来的RequestParams
        String requestParams = interfaceInfoInvokeRequest.getRequestParams();
        if(requestParams == null || "".equals(requestParams)){
            //参数错误
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //3.判断状态是否在运行
        InterfaceInfo oldinterfaceinfo = InterfaceInfoService.getById(id);

        //4.判断接口状态
        Integer status = oldinterfaceinfo.getStatus();
        if (status == 0){
            //报错 系统错误
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口已关闭");
        }

        //需要获取用户的ak和sk

        //先获取登录用户的信息
        User loginUser = userService.getLoginUser(request);

        String secretKey = loginUser.getSecretKey();
        String accessKey = loginUser.getAccessKey();



        //5.将请求参数json转为Bean对象
        Gson gson = new Gson();
        com.sdk.modal.User user = gson.fromJson(requestParams,
                com.sdk.modal.User.class);

        //需要使用当前的用户的ak/sk调用后台接口   所以需要将重新创建一个client
        JunClient junClient = new JunClient(accessKey,secretKey);

        //调用函数 返回结果
        String res = junClient.GetNameByPostAndBody(user);


        return ResultUtils.success(res);
    }




}

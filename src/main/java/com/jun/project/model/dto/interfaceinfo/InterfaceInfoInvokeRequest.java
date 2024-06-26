package com.jun.project.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 调用请求
 *
 * @TableName product
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 请求参数
     */
    private String requestParams;


}
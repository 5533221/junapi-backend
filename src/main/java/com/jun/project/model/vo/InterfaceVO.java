package com.jun.project.model.vo;

import com.junapicommon.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 接口信息的VO
 *
 * @author yupi
 * @TableName product
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceVO extends InterfaceInfo {

    /**
     * 调用的总数
     */
    private Integer totalNum;

    private static final long serialVersionUID = 1L;
}
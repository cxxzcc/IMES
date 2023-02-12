package com.itl.iap.system.api.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 版本控制表DTO
 *
 * @author zhancen
 * @date 2021-10-26
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class VersionControllerDto {
    /**
     * 分页
     */
    private Page page;

    /**
     * 主键id
     */
    private String versionId;

    /**
     *版本号
     */
    private String versionNo;

    /**
     *下载链接
     */
    private String downLoadLink;

    /**
     *是否最新版本(0否1是)
     */
    private Integer isUpToDate;

    /**
     *备注
     */
    private String remark;

    /**
     *版本新增内容日志
     */
    private String newContentLog;

    /**
     *版本修改内容日志
     */
    private Integer fixedContentLog;

    /**
     *apk上传地址
     */
    private String codeUrl;

    /**
     *版本最后下载时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDate;

    /**
     *版本发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date creationDate;

}

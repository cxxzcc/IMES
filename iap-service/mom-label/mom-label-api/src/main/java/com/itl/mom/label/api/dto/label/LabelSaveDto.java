package com.itl.mom.label.api.dto.label;

import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.CustomDataValVo;
import com.itl.mom.label.api.entity.file.MesFiles;
import com.itl.mom.label.api.entity.label.LabelEntityParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2020/12/29
 * @since JDK1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelSaveDto {

    private String id;

    private String labelTypeId;

    private String site;

    private Integer useDataSource;

    private String label;

    private String labelDescription;

    private String state;


    private String templateType;

    private String lodopText;

    private MesFiles jasperFile;

    private List<LabelEntityParams> labelEntityParamsList;

    private List<CustomDataAndValVo> customDataAndValVoList;

    private List<CustomDataValVo> customDataValVoList;
}

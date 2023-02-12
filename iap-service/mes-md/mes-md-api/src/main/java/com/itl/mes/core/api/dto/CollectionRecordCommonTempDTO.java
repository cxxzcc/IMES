package com.itl.mes.core.api.dto;

import com.itl.mes.core.api.entity.ProductMaintenanceRecord;
import com.itl.mes.core.api.entity.ProductionCollectionRecord;
import com.itl.mes.core.api.entity.ProductionDefectRecord;
import com.itl.mes.core.api.entity.RecordOfProductTest;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 采集记录通用临时数据结构
 * @author dengou
 * @date 2021/11/24
 */
@Data
@Accessors(chain = true)
public class CollectionRecordCommonTempDTO {

    /**
     * 条码
     * */
    private String sn;
    /**
     * 工厂
     * */
    private String site;
    /**
     * 车间
     * */
    private String workShop;
    /**
     * 工位
     * */
    private String station;
    /**
     * 工单编号
     * */
    private String shopOrder;
    /**
     * 当前操作人
     * */
    private String userName;

    /**
     * 生产采集记录,可以不用填，默认取值，填了就必须全部填
     * */
    private ProductionCollectionRecord productionCollectionRecord;

    /**
     * 产品检验记录
     * */
    private List<RecordOfProductTest> recordOfProductTests;

    /**
     * 产品维修记录
     * */
    private List<ProductMaintenanceRecord> productMaintenanceRecords;

    /**
     * 产品缺陷记录
     * */
    private List<ProductionDefectRecord> productionDefectRecords;

}

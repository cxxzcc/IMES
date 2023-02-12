package com.itl.mes.core.api.bo;

import com.itl.mes.core.api.constant.BOPrefixEnum;

public class QualityPlanParameterBO {
    private String bo;
    private String site;
    private String qualityPlan;
    private String version;
    private String parameterName;

    private static final String PREFIX = BOPrefixEnum.QPP.getPrefix();


    public QualityPlanParameterBO(String bo){
        this.bo=bo;
        String[] split = bo.split(":")[1].split(",");
        this.site=split[0];
        this.qualityPlan = split[1];
        this.version = split[2];
        this.parameterName = split[3];
    }

    public QualityPlanParameterBO(String site, String qualityPlan, String version, String parameterName){
        this.site =site;
        this.qualityPlan= qualityPlan;
        this.version = version;
        this.parameterName = parameterName;
        this.bo=new StringBuilder(PREFIX).append(":").append(site).append(",").append(qualityPlan).append(",").append(version).append(",").append(parameterName).toString();
    }

    public String getVersion() {
        return version;
    }

    public String getBo() {
        return bo;
    }

    public String getSite() {
        return site;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getQualityPlan() {
        return qualityPlan;

    }
}

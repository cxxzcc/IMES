
package com.itl.mes.andon.provider.config;

/**
 * 安灯相关枚举
 */
public class Constant {

    /**
     * 安灯类型标识,0代表物料，1代表设备
     */
    public static final String andonTypeTagItem = "0";

    public static final String andonTypeTagDevice = "1";

    public enum andonTypeTag {

        ITEM("0", "物料安灯"),

        DEVICE("1", "设备安灯");

        private String value;

        private String desc;

        private andonTypeTag(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 安灯资源类型常量,0代表其他，4代表设备，6代表物料
     */
    public static final String resourceTypeOther = "0";

    public static final String resourceTypeDevice = "4";

    public static final String resourceTypeItem = "6";


    /**
     * 安灯状态
     */
    public enum andonState {

        ENABLE("1", "启用"),

        DISABLE("0", "禁用");

        private final String value;
        private final String desc;

        private andonState(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 安灯资源类型
     */
    public enum andonResourceType {

        OTHER("0", "其他"),
        SITE("1", "工厂"),
        WORK_SHOP("2", "车间"),
        PRODUCT_LINE("3", "产线"),
        DEVICE("4", "设备"),
        STATION("5", "工位"),
        ITEM("6", "物料"),
        QUALITY("7", "质量");

        private final String value;

        private final String desc;

        private andonResourceType(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 安灯触发-事件-记录类型
     */
    public enum recordResourceType {
        SITE("1", "工厂"),
        WORK_SHOP("2", "车间"),
        PRODUCT_LINE("3", "产线"),
        DEVICE("4", "设备"),
        STATION("5", "工位");

        private final String value;

        private final String desc;

        private recordResourceType(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 安灯触发-事件-记录状态
     */
    public enum recordState {
        TRIGGER("1", "触发异常"),
        REPAIT("2", "修复异常");

        private final String value;

        private final String desc;

        private recordState(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

    }


}

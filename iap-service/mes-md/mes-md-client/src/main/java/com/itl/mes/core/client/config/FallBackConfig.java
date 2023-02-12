package com.itl.mes.core.client.config;

import com.itl.mes.core.client.service.*;
import com.itl.mes.core.client.service.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
@Configuration
public class FallBackConfig {

    @Bean
    public MdReportFeignImpl mdReportFeign() {
        return new MdReportFeignImpl();
    }

    @Bean
    public WorkShopServiceImpl workShopService() {
        return new WorkShopServiceImpl();
    }

    @Bean
    public ItemFeignServiceImpl itemFeignService() {
        return new ItemFeignServiceImpl();
    }

    @Bean
    public ProductLineServiceImpl productLineService() {
        return new ProductLineServiceImpl();
    }

    @Bean
    public StationServiceImpl stationService() {
        return new StationServiceImpl();
    }

    @Bean
    public DeviceServiceImpl deviceService() {
        return new DeviceServiceImpl();
    }

    @Bean
    public DeviceTypeServiceImpl deviceTypeService() {
        return new DeviceTypeServiceImpl();
    }

    @Bean
    public CustomDataValServiceImpl customDataValService() {
        return new CustomDataValServiceImpl();
    }

    @Bean
    public NgCodeServiceFeignImpl ngCodeServiceFeign() {
        return new NgCodeServiceFeignImpl();
    }

    @Bean
    public RouterServiceImpl routerServiceFeign() {
        return new RouterServiceImpl();
    }

    @Bean
    public OrderRouterServiceImpl orderRouterService() {
        return new OrderRouterServiceImpl();
    }

    @Bean
    public RouterFitServiceImpl routerFitService() {
        return new RouterFitServiceImpl();
    }

    @Bean
    public LabelRuleQueryServiceImpl labelRuleQueryService() {
        return new LabelRuleQueryServiceImpl();
    }

    @Bean
    public CodeRuleServiceImpl codeRuleService() {
        return new CodeRuleServiceImpl();
    }

    @Bean
    public BomServiceImpl bomService() {
        return new BomServiceImpl();
    }

    @Bean
    public SiteServiceImpl SiteServiceFeign() {
        return new SiteServiceImpl();
    }

    @Bean
    public ShopOrderServiceImpl shopOrderService() {
        return new ShopOrderServiceImpl();
    }

    @Bean
    public ShopOrderTwoServiceImpl shopOrderTwoService() {
        return new ShopOrderTwoServiceImpl();
    }

    @Bean
    public PackingServiceImpl packingService() {
        return new PackingServiceImpl();
    }

    @Bean
    public SnServiceImpl SnService() {
        return new SnServiceImpl();
    }

    @Bean
    public ShopOrderBomComponentServiceImpl shopOrderBomComponentService() {
        return new ShopOrderBomComponentServiceImpl();
    }

    @Bean
    public ItemServiceImpl itemService(){
        return new ItemServiceImpl();
    }

    @Bean
    public ProductionOrderServiceImpl productionOrderService(){
        return new ProductionOrderServiceImpl();
    }

    @Bean
    public MeProductInspectionItemsOrderServiceImpl MeProductInspectionItemsOrderService(){
        return new MeProductInspectionItemsOrderServiceImpl();
    }

    @Bean
    public ConfigItemServiceImpl configItemService(){
        return new ConfigItemServiceImpl();
    }

    @Bean
    public TemporaryDataService temporaryDataService(){
        return new TemporaryDataServiceImpl();
    }

    @Bean
    public CollectionRecordService collectionRecordService(){
        return new CollectionRecordServiceImpl();
    }

    @Bean
    public ClassFrequencyService ClassFrequencyService(){
        return new ClassFrequencyServiceImpl();
    }

    @Bean
    public TemporaryDataRetryLogServiceImpl temporaryDataRetryLogService(){
        return new TemporaryDataRetryLogServiceImpl();
    }
    @Bean
    public OperationService operationService(){
        return new OperationServiceImpl();
    }

    @Bean
    public MeProductInspectionItemsOrderNcCodeService meProductInspectionItemsOrderNcCodeService(){
        return new MeProductInspectionItemsOrderNcCodeServiceImpl();
    }

    @Bean
    public CallSapApiService callSapApiService(){
        return new CallSapApiServiceImpl();
    }
}

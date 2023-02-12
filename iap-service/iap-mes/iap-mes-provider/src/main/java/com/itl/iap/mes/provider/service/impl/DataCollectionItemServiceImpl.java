package com.itl.iap.mes.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.dto.MesFilesVO;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.iap.common.util.UUID;
import com.itl.iap.mes.api.entity.DataCollectionItem;
import com.itl.iap.mes.api.entity.DataCollectionItemListing;
import com.itl.iap.mes.api.entity.MesFiles;
import com.itl.iap.mes.provider.mapper.DataCollectionItemListingMapper;
import com.itl.iap.mes.provider.mapper.DataCollectionItemMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DataCollectionItemServiceImpl {

    @Autowired
    private DataCollectionItemMapper dataCollectionItemMapper;

    @Autowired
    private DataCollectionItemListingMapper dataCollectionItemListingMapper;

    @Autowired
    private FileUploadServiceImpl fileUploadService;

    public IPage<DataCollectionItem> findList(String dataCollectionId, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum, pageSize);
        return dataCollectionItemMapper.pageQuery(page, dataCollectionId);
    }


    public DataCollectionItem findById(String id) {
        DataCollectionItem dataCollectionItem = dataCollectionItemMapper.selectById(id);
        //判断imgSrc是否为空

        ResponseData<List<MesFiles>> fileList = fileUploadService.getFileList(id);
        if (ResultResponseEnum.SUCCESS.getCode().equals(fileList.getCode())) {
            List<MesFiles> data = fileList.getData();
            List<MesFilesVO> mesFilesVOs = new ArrayList<>();

            if(Boolean.FALSE.equals(CollectionUtils.isEmpty(data))){
                mesFilesVOs = data.stream().map(x -> {
                    MesFilesVO mesFilesVO = new MesFilesVO();
                    BeanUtils.copyProperties(x, mesFilesVO);
                    return mesFilesVO;
                }).collect(Collectors.toList());
                MesFiles mesFiles = data.get(0);


                dataCollectionItem.setMesFiles(mesFilesVOs);
            }


        }
        QueryWrapper<DataCollectionItemListing> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dataCollectionItemId", dataCollectionItem.getId());
        List<DataCollectionItemListing> dataCollectionItemListingList = dataCollectionItemListingMapper.selectList(queryWrapper);
        List<DataCollectionItemListing> list = dataCollectionItemListingList.stream().sorted(Comparator.comparing(DataCollectionItemListing::getSerial)).collect(Collectors.toList());
        dataCollectionItem.setDataCollectionItemListingList(list);
        return dataCollectionItem;
    }

    public ArrayList<DataCollectionItem> getList(String id) {
        final List<String> list = dataCollectionItemMapper.getList(id);
        ArrayList<DataCollectionItem> dataCollectionItems = new ArrayList<>();
        list.forEach(x -> dataCollectionItems.add(findById(x)));
        return dataCollectionItems;
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void save(DataCollectionItem dataCollectionItem) {
        List<MesFilesVO> mesFiles = dataCollectionItem.getMesFiles();
        if (Boolean.FALSE.equals(CollectionUtils.isEmpty(mesFiles))) {
            dataCollectionItem.setImgSrc(mesFiles.get(0).getFilePath());

        }else{
            dataCollectionItem.setImgSrc("");
        }
        if (StringUtils.isNotBlank(dataCollectionItem.getId())) {
            dataCollectionItemMapper.updateById(dataCollectionItem);
        } else {
            dataCollectionItem.setId(UUID.uuid32());
            dataCollectionItemMapper.insert(dataCollectionItem);
        }
        //获取图片列表

        ResponseData resp = fileUploadService.saveFilesForFeign1(mesFiles, dataCollectionItem.getId());
        if (Boolean.FALSE.equals(ResultResponseEnum.SUCCESS.getCode().equals(resp.getCode()))) {
            throw new CommonException(resp.getMsg(), CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        saveItemListing(dataCollectionItem);
    }

    private void saveItemListing(DataCollectionItem dataCollectionItem) {
        List<DataCollectionItemListing> dataCollectionItemListingList = dataCollectionItem.getDataCollectionItemListingList();
        QueryWrapper<DataCollectionItemListing> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dataCollectionItemId", dataCollectionItem.getId());
        dataCollectionItemListingMapper.delete(queryWrapper);
        if (dataCollectionItemListingList != null && !dataCollectionItemListingList.isEmpty()) {
            dataCollectionItemListingList.forEach(dataCollectionItemListing -> {
                dataCollectionItemListing.setId(null);
                dataCollectionItemListing.setDataCollectionItemId(dataCollectionItem.getId());
                dataCollectionItemListingMapper.insert(dataCollectionItemListing);
            });
        }
    }

    @Transactional
    public void delete(List<String> ids) {
        ids.forEach(id -> {
            QueryWrapper<DataCollectionItemListing> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dataCollectionItemId", id);
            List<DataCollectionItemListing> dataCollectionItemListingList = dataCollectionItemListingMapper.selectList(queryWrapper);
            if (dataCollectionItemListingList != null && !dataCollectionItemListingList.isEmpty()) {
                dataCollectionItemListingList.forEach(dataCollectionItemListing -> {
                    dataCollectionItemListingMapper.deleteById(dataCollectionItemListing.getId());
                });
            }
            dataCollectionItemMapper.deleteById(id);
        });
    }
}

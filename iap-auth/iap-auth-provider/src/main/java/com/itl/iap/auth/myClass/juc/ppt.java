package com.itl.iap.auth.myClass.juc;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.spire.presentation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ppt {
    public static void main(String[] args) throws Exception {

        final String destFilePath = "D:\\BaiduNetdiskDownload\\111\\副本123～.xlsx";
        final ExcelReader reader2 = ExcelUtil.getReader(destFilePath, 2);
        final ExcelReader reader1 = ExcelUtil.getReader(destFilePath, 1);
        final ExcelWriter writer = ExcelUtil.getWriter(destFilePath, "Sheet2");
        final List<Map<String, Object>> maps2 = reader2.readAll();
        final List<Map<String, Object>> maps1 = reader1.readAll();

        //加载PPT
        Presentation ppt = new Presentation();
        ppt.loadFromFile("D:\\BaiduNetdiskDownload\\111\\teste_1.pptx");


//        StringBuilder buffer = new StringBuilder();

        int i = 0;
        int j = 1;
        int k = 0;
        final HashMap<String, String> strings = new HashMap<>();
        for (Object slide : ppt.getSlides()) {
            final String s = ((ISlide) slide).getNotesSlide().getNotesTextFrame().getText();
            for (Object shape : ((ISlide) slide).getShapes()) {
                if (shape instanceof IAutoShape) {
                    final ITextFrameProperties textFrame = ((IAutoShape) shape).getTextFrame();
                    for (Object tp : textFrame.getParagraphs()) {
                        final String text = ((ParagraphEx) tp).getText();
                        if (i == j) {

                            strings.put(text + "," + s, maps2.get(k).get("13").toString());
                            k++;
                            j = j + 3;
                        }
                        i++;
                    }
                }
            }
        }

        System.out.println(strings);


        for (Map<String, Object> stringObjectMap : maps1) {
            final String s = stringObjectMap.get("1").toString();
            for (Map.Entry<String, String> ss : strings.entrySet()) {
                if (ss.getKey().contains(s)) {
                    for (Map.Entry<String, Object> so : stringObjectMap.entrySet()) {
                        final String key = so.getKey();
                        if (ss.getKey().contains(key)) {
                            so.setValue(ss.getValue());
                        }
                    }
                }
            }
        }
        System.out.println(maps1);

        writer.write(maps1).flush();
    }
}
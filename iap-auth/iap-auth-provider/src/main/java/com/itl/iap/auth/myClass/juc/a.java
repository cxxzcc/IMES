package com.itl.iap.auth.myClass.juc;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

import java.io.File;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * @author cch
 * @date 2021/8/17$
 * @since JDK1.8
 */
public class a {


    public static void main(String[] args) {
//        String url = "D:\\BaiduNetdiskDownload\\111";
//        move(url);
//
//
//        del(url);

        final String s = "D:\\BaiduNetdiskDownload\\111\\工作簿3.xlsx";


        final ExcelReader reader1 = ExcelUtil.getReader(s, 0);
        final ExcelReader reader2 = ExcelUtil.getReader(s, 1);

        final ExcelWriter writer = ExcelUtil.getWriter(s, "Resultado");

        final List<Map<String, Object>> maps1 = reader1.readAll();


        final HashMap<String, Object> stringStringHashMap = new HashMap<>();
        final HashMap<String, Object> SS = new HashMap<>();
        final List<String> strings = Arrays.asList("AA", "BB", "AB", "BA", "AC", "BC", "CA", "CB", "CC", "CD");


        for (Map<String, Object> stringObjectMap : maps1) {

            final HashMap<String, Integer> stringIntegerHashMap = new HashMap<String, Integer>();

            for (String string : strings) {
                final String s2 = Optional.ofNullable(stringObjectMap.get(string)).orElse("").toString();
                if (StrUtil.isNotBlank(s2)&&s2.contains("{")) {

                    for (String s1 : s2.replace("}", "").replace("{", "").split(",")) {
                        final String[] split = s1.split("=");

                            final String obj = split[0].trim();
                            stringIntegerHashMap.put(obj, stringIntegerHashMap.getOrDefault(obj, 0) + Integer.valueOf(split[1]));

                    }
                }
            }
            stringObjectMap.put("all", stringIntegerHashMap);
//            final Integer a1 = stringIntegerHashMap.get("A");
//            final Integer b1 = stringIntegerHashMap.get("B");
//            Integer c1 = 250 - a1 - b1;
//            stringIntegerHashMap.put("C", c1);
//            stringStringHashMap.put(string, stringIntegerHashMap.toString());
//            final BigDecimal a = new BigDecimal(a1).divide(new BigDecimal("2.50"), 2, BigDecimal.ROUND_HALF_UP);
//            final BigDecimal b = new BigDecimal(b1).divide(new BigDecimal("2.50"), 2, BigDecimal.ROUND_HALF_UP);
//            final BigDecimal c = new BigDecimal(c1).divide(new BigDecimal("2.50"), 2, BigDecimal.ROUND_HALF_UP);

//            DecimalFormat df = new DecimalFormat("###.##");
//            df.format(a);
//            df.format(b);
//            df.format(c);

//            SS.put(string, a + "%," + b + "%," + c + "%");

            System.out.println(stringIntegerHashMap);
        }
        writer.write(maps1).flush();
    }


    private static void del(String url) {
        FileUtil.walkFiles(new File(url).toPath(), -1, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                final File file = path.toFile();
                final String name = file.getName();
                if (name.contains("【如花视频-www.rhsp.pw】-")) {
                    FileUtil.rename(file, name.replace("【如花视频-www.rhsp.pw】-", ""), false);
                }
                if (name.endsWith("删除")) {
                    FileUtil.rename(file, name.replace("删除", ""), false);
                }
                if (name.contains("下载！")
                        || name.contains("打不开")
                        || name.contains("m下载")
                        || name.contains("www.huotuji.com .jpg")
                        || name.contains("maozhua.org")
                        || name.contains("火图集最新网址发布页")
//                        || name.contains("jpg")
//                        || name.contains("JPG")
                ) {
                    FileUtil.del(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void move(String url) {
        FileUtil.walkFiles(new File(url).toPath(), -1, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                final File file = path.toFile();
                final String name = file.getName();
                if (name.endsWith("zip") || name.endsWith(".7z")) {
                    FileUtil.move(file, new File(url), false);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

}

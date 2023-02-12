package com.itl.iap.common.util;

import com.alibaba.fastjson.JSONObject;
import com.itl.iap.common.base.aop.JdbcConfig;
import com.itl.iap.common.base.utils.LogUtil;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * http 请求工具类
 *
 * @author 汤俊
 * @date 2021-12-31
 * @since jdk1.8
 */
@Log4j2
public class HttpClient {

    @Resource
    JdbcConfig jdbcConfig;

    // 交互接口日志工具类
    // private static LogUtil logUtil;
//    @Autowired
//    private LogUtil logUtil;

    public static String doGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        // 返回结果字符串
        String result = null;
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }

    public static String doPost(String httpUrl, String param) {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);

            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            os.write(param.getBytes());
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {

                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return result;
    }

    public static String doPost(String routing, String account, String password, String param, JdbcConfig jdbcConfig) {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        // 交互接口日志(默认：成功记录)
        Boolean fl = true;
        JSONObject rseObject = null;
        try {
            URL url = new URL(routing);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);

            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);

            // 设置传入参数的格式
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8;");
            //设置请求标识
            connection.setRequestProperty("reqId", UUID.uuid32());
            //设置请求系统名称
            connection.setRequestProperty("reqFrom", "WMS");

            // 设置鉴权信息
            // connection.setRequestProperty("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString((account + ":" + password).getBytes()));
            // connection.setRequestProperty("Authorization", "Basic eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb25nX3Rva2VuIiwiY2xpZW50SWQiOiJmM2VkMGQ5MC0zZmM4LTRlYmEtYjgzYy1hMGNmZTU0YzE3NGQiLCJzYWx0IjoiYWRtaW4iLCJpc3MiOiJJc3N1ZXIiLCJleHAiOjE2NDEzNTAzNzEsImlhdCI6MTY0MTI2Mzk3MSwianRpIjoiYzM3ZGM5OGMxMDA2NGIwNzk2ZjM0NWU3MTI2ZGEyZWQiLCJ1c2VybmFtZSI6ImFkbWluIn0.ujp9Zzl-4ApDSg1ciFmPZEy3CiOK6AoxhZh2ONpltX8");

            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            os.write(param.getBytes());
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

                StringBuilder sbf = new StringBuilder();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
                rseObject = JSONObject.parseObject(result);
                if (!"200".equals(rseObject.getString("code"))) {
                    LogUtil.log("交互接口", param, routing, rseObject.getString("code"), "1", "1", "1",jdbcConfig);
                }
            } else {
                log.error("===========================>SAP接口异常:" + connection.getErrorStream());
                LogUtil.log("交互接口", param, routing, "SAP接口异常!", "2", "1", "1", jdbcConfig);
                throw new RuntimeException("SAP接口异常!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 异常
            fl = false;
            LogUtil.log("交互接口", param, routing, e.getMessage(), "2", "1", "1",jdbcConfig);
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // 异常
                    fl = false;
                    LogUtil.log("交互接口", param, routing, e.getMessage(), "2", "1", "1",jdbcConfig);
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // 异常
                    fl = false;
                    LogUtil.log("交互接口", param, routing, e.getMessage(), "2", "1", "1",jdbcConfig);
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // 异常
                    fl = false;
                    LogUtil.log("交互接口", param, routing, e.getMessage(), "2", "1", "1",jdbcConfig);
                }
            }
            // 断开与远程地址url的连接
            Objects.requireNonNull(connection).disconnect();
        }
        /**
         * executionFlag:是否成功：0成功 1失败
         * 备注：此处由于不确定返回结果状态，所以默认为成功：0
         */
        LogUtil.log("交互接口", param, routing, rseObject.getString("code"), "1", "1", "0",jdbcConfig);
        return result;
    }

    public static String doPostAndLog(String routing, String account, String password, String param, JdbcConfig jdbcConfig) {
        JSONObject rseObject = null;
        try {
//            if (querySilentPeriodSet(JSON.toJSONString(productionStorageDto), url + PRODUCTION_STORAGE_ROUTING, "1", PRODUCE_CODE)) {
//                return ResponseData.success("静默期开启延缓执行!");
//            }
            String resStr = doPost(routing, account, password, param,jdbcConfig);
            rseObject = JSONObject.parseObject(resStr);
            // 0：成功
            if ("S".equals(rseObject.getString("RTNCODE"))) {
                LogUtil.log("交互接口", param, routing, rseObject.getString("RTNMSG"), "1", "1", "0",jdbcConfig);
                return rseObject.getString("RTNMSG");
            }
        } catch (Exception e) {
            LogUtil.log("交互接口", param, routing, e.getMessage(), "2", "1", "1",jdbcConfig);
            return e.getMessage();
        }
        // 1：失败
        LogUtil.log("交互接口", param, routing, rseObject.getString("RTNMSG"), "1", "1", "1",jdbcConfig);
        return rseObject.getString("RTNMSG");
    }
}

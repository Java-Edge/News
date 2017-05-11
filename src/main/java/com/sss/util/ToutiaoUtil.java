package com.sss.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Map;

/**
 * Created by Shusheng Shi on 2017/5/9.
 */
//此工具类都为了将数据如何转化给前端看
public class ToutiaoUtil {
    private static final Logger logger = LoggerFactory.getLogger(ToutiaoUtil.class);

    public static String TOUTIAO_DOMAIN = "http://127.0.0.1:8080/";
//  文件上传目录
    public static String IMAGE_DIR = "F:/upload/";
    public static String[] IMAGE_FILE_EXT = new String[]{"png", "bmp", "jpg", "jpeg"};

    /**
     * 判断文件是否允许上传
     * @param fileExt :文件扩展名
     * @return   true表允许上传
     */
    public static boolean isFileAllowed(String fileExt) {
        for (String ext : IMAGE_FILE_EXT) {
            if (ext.equals(fileExt)) {
                return true;
            }
        }
        return false;
    }
    //  JSON串打包工具

    /**
     *
     * @param code:服务器端返回状态(0表正常,1表服务器内部系统错误)
     * @return:json对象
     */
    public static String getJSONString(int code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }

//  JSON中有多个字段时,用map保存
    public static String getJSONString(int code, Map<String ,Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String ,Object> entry:map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toJSONString();
    }

//  放入一些消息
    public static String getJSONString(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);

        return json.toJSONString();
    }
    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }
}

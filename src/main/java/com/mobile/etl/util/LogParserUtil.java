package com.mobile.etl.util;

import com.mobile.common.LogConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName LogParserUtil
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description 解析日志
 **/
public class LogParserUtil {
    private static final Logger logger = Logger.getLogger(LogParserUtil.class);
    static Map<String,String> info = new ConcurrentHashMap<String,String>();
    /**
     *
     * @param log  ip^Atimestamp^Ahost^Arequestbody
     *             114.61.94.253^A1537257783.123^Ahh^A/BCImg.gif?en=e_l&ver=1&pl=website&sdk=js&u_ud=27F69684-BBE3-42FA-AA62-71F98E208444&u_mid=Aidon&u_sd=38F66FBB-C6D6-4C1C-8E05-72C31675C00A&c_time=1449917532123&l=zh-CN&b_iev=Mozilla%2F5.0%20(Windows%20NT%206.1%3B%20WOW64)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Chrome%2F46.0.2490.71%20Safari%2F537.36&b_rst=1280*768
     * @return map
     */
    public static Map<String,String> parserLog(String log){
        if(StringUtils.isEmpty(log)){
            logger.info("log is null.");
            return null;
        }
        //log正常
        String[] fields = log.split(LogConstants.SPARTOR);
        if(fields.length == 4){
            info.put(LogConstants.EVENT_COLUMN_NAME_IP,fields[0]);
            info.put(LogConstants.EVENT_COLUMN_NAME_SERVER_TIME,
                    fields[1].replace("\\.",""));
            String requestBody = fields[3];//请求参数列表
            handleRequestBody(info,requestBody);
            //解析ip
            handleIp(info);
            //解析userAgent
            handleUserAgent(info);
        }
        return info;
    }


    /**
     * 解析ip
     * @param info
     */
    private static void handleIp(Map<String,String> info) {
        if(info.containsKey(LogConstants.EVENT_COLUMN_NAME_IP)){
            //获取ip并解析
            IPParserUtil.RegionInfo region = IPParserUtil.parserIp(info.get(LogConstants.EVENT_COLUMN_NAME_IP));
            //设置值
            info.put(LogConstants.EVENT_COLUMN_NAME_COUNTRY,region.getCountry());
            info.put(LogConstants.EVENT_COLUMN_NAME_PROVINCE,region.getProvince());
            info.put(LogConstants.EVENT_COLUMN_NAME_CITY,region.getCity());
        }
    }

    /**
     * 解析ua
     * @param info
     */
    private static void handleUserAgent(Map<String,String> info) {
        if(info.containsKey(LogConstants.EVENT_COLUMN_NAME_USERAGENT)){
            //获取ip并解析
            UserAgentUtil.UserAgentInfo ua = UserAgentUtil.parserUserAgent(info.get(LogConstants.EVENT_COLUMN_NAME_USERAGENT));
            //设置值
            info.put(LogConstants.EVENT_COLUMN_NAME_BROWSER_NAME,ua.getBrowserName());
            info.put(LogConstants.EVENT_COLUMN_NAME_BROWSER_VERSION,ua.getBrowserVersion());
            info.put(LogConstants.EVENT_COLUMN_NAME_OS_NAME,ua.getOsName());
            info.put(LogConstants.EVENT_COLUMN_NAME_OS_VERSION,ua.getOsVersion());
        }
    }



    /**
     * 处理请求参数列表
     * @param info
     * @param requestBody
     */
    private static void handleRequestBody(Map<String,String> info, String requestBody) {
        if(StringUtils.isNotEmpty(requestBody)){
            int index = requestBody.indexOf("?");
            if(index > 0){
                requestBody = requestBody.substring(index+1,requestBody.length());
                String[] fields = requestBody.split("&");
                for (String field : fields){
                    String[] kvs = field.split("=");
                    String k = kvs[0];
                    String v = kvs[1];
                    if(StringUtils.isNotEmpty(k)){
                        try {
                            info.put(k,URLDecoder.decode(v,"utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            logger.warn("value解码异常.",e);
                        }
                    }
                }
            }
        }
    }
}
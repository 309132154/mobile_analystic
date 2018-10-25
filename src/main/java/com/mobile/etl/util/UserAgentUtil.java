package com.mobile.etl.util;


import com.cloudera.io.netty.util.internal.StringUtil;
import cz.mallat.uasparser.OnlineUpdateUASparser;
import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @ClassName UserAgentUtil
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description 浏览器代理对象解析
 **/
public class UserAgentUtil {
    private static final Logger logger = Logger.getLogger(UserAgentUtil.class);

    private static UASparser uaSparser = null;

    //初始化
    static {
        try {
            uaSparser = new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (IOException e) {
            logger.warn("初始化异常",e);
        }
    }

    /**
     *
     * @param useragent 解析参数
     * @return
     */
    public static UserAgentInfo parserUserAgent(String useragent){
        UserAgentInfo info = null;
        if(StringUtils.isEmpty(useragent)){
            logger.info("useragent is null");
            return null;
        }
        try {
            //正常解析
            cz.mallat.uasparser.UserAgentInfo in = uaSparser.parse(useragent);
            if(in != null){
                //设置值
                info = new UserAgentInfo();
                info.setBrowserName(in.getUaFamily());
                info.setBrowserVersion(in.getBrowserVersionInfo());
                info.setOsName(in.getOsFamily());
                info.setOsVersion(in.getOsName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 用于封装解析后的信息
     */
    public static class UserAgentInfo{
        private String browserName;
        private String browserVersion;
        private String osName;
        private String osVersion;

        public String getBrowserName() {
            return browserName;
        }

        public void setBrowserName(String browserName) {
            this.browserName = browserName;
        }

        public String getBrowserVersion() {
            return browserVersion;
        }

        public void setBrowserVersion(String browserVersion) {
            this.browserVersion = browserVersion;
        }

        public String getOsName() {
            return osName;
        }

        public void setOsName(String osName) {
            this.osName = osName;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        @Override
        public String toString() {
            return "UserAgentInfo{" +
                    "browserName='" + browserName + '\'' +
                    ", browserVersion='" + browserVersion + '\'' +
                    ", osName='" + osName + '\'' +
                    ", osVersion='" + osVersion + '\'' +
                    '}';
        }
    }

}
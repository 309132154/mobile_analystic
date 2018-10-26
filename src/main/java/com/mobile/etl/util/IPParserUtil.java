package com.mobile.etl.util;

import com.alibaba.fastjson.JSONObject;
import com.mobile.common.GlobalConstants;
import com.mobile.etl.util.ip.IPSeeker;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @ClassName IPParserUtil
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description 将每一行数据中的ip进行解析
 **/
public class IPParserUtil extends IPSeeker {
    private static final Logger logger = Logger.getLogger(IPParserUtil.class);



    /**
     *
     * @param ip  传进来的ip
     * @return  ip解析出来的国家省市对应的regionInfo对象。
     * ip为空，返回null.ip解析不出来，返回默认值unknown
     */
    public static RegionInfo parserIp(String ip){
        RegionInfo info = new RegionInfo();
        if(StringUtils.isEmpty(ip)){
            logger.info("ip is null.ip:"+ip);
            return null;
        }
        //代码走到这，ip正常
        String country = IPSeeker.getInstance().getCountry(ip);
        if(StringUtils.isNotEmpty(country)){
            //country不能于空
            if(country.equals("局域网")){
                info.setCountry("中国");
                info.setProvince("广东");
                info.setCity("深圳市");
            } else {
                //county不是局域网的情况，判断查询出来的时候有省
                int index = country.indexOf("省");
                info.setCountry("中国");
                if(index > 0){
                    info.setProvince(country.substring(0,index+1));
                    //判断是否有市
                    int index2 = country.indexOf("市");
                    if(index2 > 0){
                        //设置市
                        info.setCity(country.substring(index+1,index2+1));
                    }
                } else {
                    //如果coutry中没有省，则可能是直辖市和自治区和特区
                    String flag = country.substring(0,2);
                    switch (flag){
                        case "内蒙":
                            info.setProvince(flag+"古");
                            country = country.substring(3);
                            index = country.indexOf("市");
                            if(index > 0){
                                //设置市
                                info.setCity(country.substring(0,index+1));
                            }
                            break;
                        case "新疆":
                        case "西藏":
                        case "广西":
                        case "宁夏":
                            info.setProvince(flag);
                            country = country.substring(2);
                            index = country.indexOf("市");
                            if(index > 0){
                                //设置市
                                info.setCity(country.substring(0,index+1));
                            }
                            break;

                        case "北京":
                        case "天津":
                        case "上海":
                        case "重庆":

                            info.setProvince(flag+"市");
                            country = country.substring(2);
                            index = country.indexOf("区");
                            if(index > 0){
                                //char ch = country.charAt(index-1);
                                //if(ch != '校' && ch != '小' &&  ch != '军'){
                                    //设置区
                                    info.setCity(country.substring(0,index));
                                //}
                            }
                            index = country.indexOf("县");
                            if(index > 0){
                                //设置县
                                info.setCity(country.substring(0,index+1));
                            }
                            break;

                        case "香港":
                        case "澳门":
                            info.setProvince(flag+"特别行政区");
                            break;
                        case "台湾":
                            info.setProvince(flag+"省");
                            break;

                            default:
                                //info.setCountry(country);
                                break;
                    }
                }
            }
        }
        return info;
    }


    /**
     * 使用淘宝ip解析
     * @param url
     * @param charset
     * @return
     * @throws Exception
     */
    public RegionInfo parserIp1(String url, String charset) throws Exception {
        RegionInfo info = new RegionInfo();
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);

        if (null == url || !url.startsWith("http")) {
            throw new Exception("请求地址格式不对");
        }
        // 设置请求的编码方式
        if (null != charset) {
            method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);
        } else {
            method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + "utf-8");
        }
        //发送请求
        int statusCode = client.executeMethod(method);

        if (statusCode != HttpStatus.SC_OK) {// 打印服务器返回的状态
            System.out.println("Method failed: " + method.getStatusLine());
        }
        // 返回响应消息
        byte[] responseBody = method.getResponseBodyAsString().getBytes(method.getResponseCharSet());
        // 在返回响应消息使用编码(utf-8或gb2312)
        String response = new String(responseBody, "utf-8");
        /**
         * {"code":0,"data":{"ip":"0.255.255.255","country":"XX","area":"","region":"XX","city":"内网IP","county":"内网IP","isp":"内网IP","country_id":"xx","area_id":"",
         * "region_id":"xx","city_id":"local","county_id":"local","isp_id":"local"}}
         */
       //将reponse的字符串转换成json对象
        JSONObject jo = JSONObject.parseObject(response);
        JSONObject jo1 = JSONObject.parseObject(jo.getString("data"));

        //赋值
        info.setCountry(jo1.getString("country"));
        info.setProvince(jo1.getString("region"));
        info.setCity(jo1.getString("city"));
        return info;
    }


    /**
     * 用于封装国家、省、市
     */
    public static class RegionInfo{
        private static final String DEFAULT_VALUE = GlobalConstants.DEFAULT_VALUE;
        private String country = DEFAULT_VALUE;
        private String province = DEFAULT_VALUE;
        private String city = DEFAULT_VALUE;

        public RegionInfo(){

        }

        public RegionInfo(String country, String province, String city) {
            this.country = country;
            this.province = province;
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        @Override
        public String toString() {
            return "RegionInfo{" +
                    "country='" + country + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    '}';
        }
    }
}
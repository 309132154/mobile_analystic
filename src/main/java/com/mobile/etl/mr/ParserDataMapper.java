package com.mobile.etl.mr;

import com.mobile.common.EventEnum;
import com.mobile.common.LogConstants;
import com.mobile.etl.util.LogParserUtil;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

/**
 * @ClassName ParserDataMapper
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description 将hdfs中清洗的数据存储到hdfs中
 **/
public class ParserDataMapper extends Mapper<LongWritable,Text,LogWritable,NullWritable> {
    private static final Logger logger = Logger.getLogger(ParserDataMapper.class);
    private LogWritable lw = new LogWritable();
    private int inputRecords,filterRecords,outputRecords = 0;

    public static final LogWritable LW = new LogWritable();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        logger.info(value.toString());
        this.inputRecords ++;
        String line = value.toString();
        Map<String,String> info = LogParserUtil.parserLog(line);

        //获取事件名称
        String eventName = info.get(LogConstants.EVENT_COLUMN_NAME_EVENT_NAME);
        EventEnum en = EventEnum.valueOfName(eventName);

        //根据事件获取对应的字段的时
        switch (en){
            case CHARGEREFUND:
            case CHARGESUCCESS:
            case CHARGEREQUEST:
            case PAGEVIEW:
            case EVENT:
            case LANUCH:
                //所有事件统一处理
                handleData(info,context);
        }

    }

    /**
     * 写出info中的日志
     * @param info
     * @param context
     */
    private void handleData(Map<String,String> info, Context context) {
        try {
            if(!info.isEmpty()){
                LW.s_time = info.get(LogConstants.EVENT_COLUMN_NAME_SERVER_TIME);
                LW.en = info.get(LogConstants.EVENT_COLUMN_NAME_EVENT_NAME);
                LW.ver = info.get(LogConstants.EVENT_COLUMN_NAME_VERSION);
                LW.u_ud = info.get(LogConstants.EVENT_COLUMN_NAME_UUID);
                LW.u_mid = info.get(LogConstants.EVENT_COLUMN_NAME_MEMBER_ID);
                LW.u_sid = info.get(LogConstants.EVENT_COLUMN_NAME_SESSION_ID);
                LW.c_time = info.get(LogConstants.EVENT_COLUMN_NAME_CLIENT_TIME);
                LW.language = info.get(LogConstants.EVENT_COLUMN_NAME_LANGUAGE);
                LW.b_iev = info.get(LogConstants.EVENT_COLUMN_NAME_USERAGENT);
                LW.b_rst = info.get(LogConstants.EVENT_COLUMN_NAME_RESOLUTION);
                LW.p_url = info.get(LogConstants.EVENT_COLUMN_NAME_CURRENT_URL);
                LW.p_ref = info.get(LogConstants.EVENT_COLUMN_NAME_PREFFER_URL);
                LW.tt = info.get(LogConstants.EVENT_COLUMN_NAME_TITLE);
                LW.pl = info.get(LogConstants.EVENT_COLUMN_NAME_PLATFORM);
                LW.oid = info.get(LogConstants.EVENT_COLUMN_NAME_ORDER_ID);
                LW.on = info.get(LogConstants.EVENT_COLUMN_NAME_ORDER_NAME);
                LW.cut = info.get(LogConstants.EVENT_COLUMN_NAME_CURRENCY_TYPE);
                LW.cua = info.get(LogConstants.EVENT_COLUMN_NAME_CURRENCY_AMOUTN);
                LW.pt = info.get(LogConstants.EVENT_COLUMN_NAME_PAYMENT_TYPE);
                LW.ca = info.get(LogConstants.EVENT_COLUMN_NAME_EVENT_CATEGORY);
                LW.ac = info.get(LogConstants.EVENT_COLUMN_NAME_EVENT_ACTION);
                LW.kv_ = info.get(LogConstants.EVENT_COLUMN_NAME_EVENT_KV);
                LW.du = info.get(LogConstants.EVENT_COLUMN_NAME_EVENT_DURATION);
                LW.os = info.get(LogConstants.EVENT_COLUMN_NAME_OS_NAME);
                LW.os_v = info.get(LogConstants.EVENT_COLUMN_NAME_OS_VERSION);
                LW.browser = info.get(LogConstants.EVENT_COLUMN_NAME_BROWSER_NAME);
                LW.browser_v = info.get(LogConstants.EVENT_COLUMN_NAME_BROWSER_VERSION);
                LW.country = info.get(LogConstants.EVENT_COLUMN_NAME_COUNTRY);
                LW.province = info.get(LogConstants.EVENT_COLUMN_NAME_PROVINCE);
                LW.city = info.get(LogConstants.EVENT_COLUMN_NAME_CITY);
        } else {
                this.filterRecords ++;
        }
            //输出
            this.outputRecords++;
            context.write(LW,NullWritable.get());
        } catch (Exception e) {
            this.filterRecords ++;
           e.printStackTrace();
        }
    }
}
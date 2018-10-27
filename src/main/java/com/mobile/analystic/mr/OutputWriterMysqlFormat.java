package com.mobile.analystic.mr;

import com.mobile.analystic.modle.base.BaseDimension;
import com.mobile.analystic.modle.value.StatsValueDimension;
import com.mobile.analystic.mr.service.DimensionToMysqlI;
import com.mobile.analystic.mr.service.impl.DimensionToMysqlImpl;
import com.mobile.common.GlobalConstants;
import com.mobile.common.KpiType;
import com.mobile.util.JdbcUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName OutputWriterMysqlFormat
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description 自定义msyql的输出(将最终sql语句执行)
 **/
public class OutputWriterMysqlFormat extends OutputFormat<BaseDimension,StatsValueDimension> {
//    DBOutputFormat
//    DBOutputFormat.DBRecordWriter
    private static final Logger logger = Logger.getLogger(OutputWriterMysqlFormat.class);


    /**
     * 将key-value写出
     */
    public static class OutputWriterMysqlWriter extends RecordWriter<BaseDimension,StatsValueDimension>{
       private Connection conn;
       private Configuration conf;
       private DimensionToMysqlI dimensionToMysqlI;
       //定义一个缓存 装kpi:和其个数
        Map<KpiType,Integer> batch = new ConcurrentHashMap<KpiType, Integer>();
        //定义一个缓存 kpi:ps
        Map<KpiType,PreparedStatement> map = new ConcurrentHashMap<KpiType,PreparedStatement>();

        public OutputWriterMysqlWriter(Connection conn, Configuration conf, DimensionToMysqlI dimensionToMysqlI) {
            this.conn = conn;
            this.conf = conf;
            this.dimensionToMysqlI = dimensionToMysqlI;
        }

        /**
         * 写
         * @param key
         * @param value
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        public void write(BaseDimension key, StatsValueDimension value) throws IOException, InterruptedException {
            if(key == null || value == null){
                return;
            }
            try {
                //获取ps\赋值\执行
                PreparedStatement ps = null;
                KpiType kpi = value.getKpi();
                int count = 1;
                if(map.containsKey(kpi)){
                    ps = map.get(kpi);
                    count = batch.get(kpi);
                    count ++;
                } else {
                    //缓存中没有对应的kpi的ps
                    ps = this.conn.prepareStatement(this.conf.get(kpi.kpiType));
                    this.map.put(kpi,ps);
                }
                //将batch中的数量更新
                this.batch.put(kpi,count);

                //为每一个kpi对相应的ps赋值
                //获取kpi对应的赋值的类
                //com.mobile.analystic.mr.nu.NewUserWriter
                String className = conf.get(GlobalConstants.PREFFIX_WRITTER +kpi.kpiType);
                Class<?> classz = Class.forName(className);
//                ReflectionUtils.newInstance()
                OutputWriterI outputWriterI = (OutputWriterI) classz.newInstance();
                //真正赋值的语句
                outputWriterI.writer(conf,key,value,ps,dimensionToMysqlI);

                //执行ps
                if(this.batch.size() % 50 == 0){
                    ps.executeBatch(); //批处理中会自动提交
                    conn.commit();  //如果该句报异常，去掉即可？
                    batch.remove(kpi);
                }
            } catch (Exception e) {
                logger.warn("执行结果写出异常.",e);
            }
        }

        /**
         * 关闭资源，并执行所有的缓存中写出数据
         * @param taskAttemptContext
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
            try {
                //循环将剩余的ps执行吧
                for (Map.Entry<KpiType,PreparedStatement> en : this.map.entrySet()){
                    en.getValue().executeBatch();
                }
            } catch (SQLException e) {
                logger.warn("执行剩余的sql异常.",e);
                throw new RuntimeException(e);
            } finally {
                try {
                    if(conn != null){
                        conn.commit();  //不等于空提交(批处理中已经自动提交)。如果该句报异常，去掉即可？
                    }
                } catch (SQLException e) {
                   logger.warn("提交异常.",e);
                } finally {
                    //关闭
                    //循环将剩余的已经执行的ps移除
                    for (Map.Entry<KpiType,PreparedStatement> en : this.map.entrySet()){
                        this.map.remove(en.getKey()); //移除
                        try {
                            en.getValue().close();
                        } catch (SQLException e) {
                            logger.warn("关闭剩余的ps异常.",e);
                        }
                    }
                    //关闭conn
                    JdbcUtil.close(conn,null,null);
                }
            }
        }
    }

    @Override
    public RecordWriter<BaseDimension, StatsValueDimension> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
       //准备需要的三个参数
        Connection conn = JdbcUtil.getConn();
        Configuration conf = taskAttemptContext.getConfiguration();
        DimensionToMysqlI toMysqlI = new DimensionToMysqlImpl();
        return new OutputWriterMysqlWriter(conn,conf,toMysqlI);
    }

    @Override
    public void checkOutputSpecs(JobContext jobContext) throws IOException, InterruptedException {
        //do nothing
    }

    @Override
    public OutputCommitter getOutputCommitter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new FileOutputCommitter(FileOutputFormat.getOutputPath(taskAttemptContext),taskAttemptContext);
//        return new FileOutputCommitter(null,taskAttemptContext);
    }
}
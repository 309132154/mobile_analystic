package com.mobile.analystic.mr.service.impl;

import com.mobile.analystic.modle.base.*;
import com.mobile.analystic.mr.service.DimensionToMysqlI;
import com.mobile.util.JdbcUtil;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName DimensionToMysqlImpl
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description 根据基础维度类获取维度ID的实现
 **/
public class DimensionToMysqlImpl implements DimensionToMysqlI {
    private static final Logger logger = Logger.getLogger(DimensionToMysqlImpl.class);
    //定义一个缓存 基础维度的属性:对应的维度Id
    private Map<String,Integer> cache = new LinkedHashMap<String,Integer>(){
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
            return this.size() > 5000;
        }
    };

    /**
     * 1、从缓存中获取对应的维度ID
     * 2、缓存中没有则去mysql中查询；查询如果没有则先插入，再获取。如果有则直接返回即可
     * @param dimension
     * @return
     */
    @Override
    public int getDimensionIdByDimension(BaseDimension dimension) {
        int id = -1;
        Connection conn = null;
        try {
            String cacheKey = buildCacheKey(dimension);
            if(this.cache.containsKey(cacheKey)){
                return this.cache.get(cacheKey);
            }
            //代码到这儿，证明缓存中没有
             conn = JdbcUtil.getConn();
            String[] sqls = null;
            //根据不同的维度构建不同的sql
            if(dimension instanceof PlatformDimension){
                sqls = buildPlatformSqls(dimension);
            } else if(dimension instanceof KpiDimension){
                sqls = buildKpiSqls(dimension);
            } else if(dimension instanceof DateDimension){
                sqls = buildDateSqls(dimension);
            } else if(dimension instanceof BrowserDimension){
                sqls = buildBrowserSqls(dimension);
            }
            //执行sql
            synchronized (this){
                id = executeSqls(conn,dimension,sqls);
            }
            //把结果放到缓存
            this.cache.put(cacheKey,id);
        } catch (Exception e) {
           logger.warn("执行获取维度id异常.",e);
        } finally {
            JdbcUtil.close(conn,null,null);
        }
        return id;
    }

    /**
     * 生成缓存key
     * @param dimension
     * @return
     */
    private String buildCacheKey(BaseDimension dimension) {
        StringBuffer sb = new StringBuffer();
        if(dimension instanceof PlatformDimension){
            PlatformDimension platform = (PlatformDimension) dimension;
            sb.append("platform_");
            sb.append(platform.getPlatformName());
        } else if(dimension instanceof KpiDimension){
            KpiDimension kpi = (KpiDimension) dimension;
            sb.append("kpi_");
            sb.append(kpi.getKpiName());
        } else if(dimension instanceof DateDimension){
            DateDimension date = (DateDimension) dimension;
            sb.append("date_");
            sb.append(date.getYear()).append(date.getSeason()).append(date.getMonth())
            .append(date.getWeek()).append(date.getDay()).append(date.getType()).
                    append(date.getCalendar());
        } else if(dimension instanceof BrowserDimension){
            BrowserDimension browser = (BrowserDimension) dimension;
            sb.append("browser_");
            sb.append(browser.getBrowserName()).append(browser.getBrowserVersion());
        } else {
            logger.warn("该dimension不能构建对应的cacheKey."+dimension.getClass());
        }
        return  sb.toString();
    }

    private String[] buildBrowserSqls(BaseDimension dimension) {
        String insertSql = "insert into `dimension_browser`(`browser_name`,`browser_version`) values(?,?)";
        String selectSql = "select id from `dimension_browser` where `browser_name` = ? and `browser_version` = ?";
        return new String[]{insertSql,selectSql};
    }

    private String[] buildDateSqls(BaseDimension dimension) {
        String insertSql = "insert into `dimension_date`(`year`,`season`, `month`,`week`,`day`,`calendar`,`type`) values(?,?,?,?,?,?,?)";
        String selectSql = "select id from `dimension_date` where `year` = ? and `season` = ? and `month` = ? and `week` = ? and `day` = ? and `calendar` = ? and `type` = ?";
        return new String[]{insertSql,selectSql};
    }

    private String[] buildKpiSqls(BaseDimension dimension) {
        String insertSql = "insert into `dimension_kpi`(`kpi_name`) values(?)";
        String selectSql = "select id from `dimension_kpi` where `kpi_name` = ?";
        return new String[]{insertSql,selectSql};
    }

    private String[] buildPlatformSqls(BaseDimension dimension) {
        String insertSql = "insert into `dimension_platform`(`platform_name`) values(?)";
        String selectSql = "select id from `dimension_platform` where `platform_name` = ?";
        return new String[]{insertSql,selectSql};
    }

    /**
     * 执行sql
     * @param conn
     * @param dimension
     * @param sqls
     * @return
     */
    private int executeSqls(Connection conn, BaseDimension dimension, String[] sqls) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //先执行查询，没有才能插入返回
            ps = conn.prepareStatement(sqls[1]);
            setArgs(ps,dimension); //赋值
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
            //代码走到这儿，没有查询到；先插入再返回id
            ps = conn.prepareStatement(sqls[0],Statement.RETURN_GENERATED_KEYS);
            setArgs(ps,dimension); //赋值
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();  //获取返回的key
            if(rs.next()){
                return rs.getInt(1);
            }

        } catch (SQLException e) {
           logger.warn("执行插入或者查询的sql异常.",e);
        } finally {
            JdbcUtil.close(null,ps,rs);
        }
        throw new RuntimeException("执行sql异常.");
    }

    /**
     * 赋值
     * @param ps
     * @param dimension
     */
    private void setArgs(PreparedStatement ps, BaseDimension dimension) {
        int i = 0;
        try {
            if(dimension instanceof PlatformDimension){
                PlatformDimension platformDimension = (PlatformDimension) dimension;
                ps.setString(++i,platformDimension.getPlatformName());
            } else if(dimension instanceof KpiDimension){
                KpiDimension kpi = (KpiDimension) dimension;
                ps.setString(++i,kpi.getKpiName());
            } else if(dimension instanceof BrowserDimension){
                BrowserDimension browser = (BrowserDimension) dimension;
                ps.setString(++i,browser.getBrowserName());
                ps.setString(++i,browser.getBrowserVersion());
            } else if(dimension instanceof DateDimension){
                DateDimension date = (DateDimension) dimension;
                ps.setInt(++i,date.getYear());
                ps.setInt(++i,date.getSeason());
                ps.setInt(++i,date.getMonth());
                ps.setInt(++i,date.getWeek());
                ps.setInt(++i,date.getDay());
                ps.setDate(++i,new Date(date.getCalendar().getTime())); //sql的date
                ps.setString(++i,date.getType());
            }
        } catch (SQLException e) {
            logger.warn("插入或者是查询的sql语句赋值异常.",e);
        }
    }
}
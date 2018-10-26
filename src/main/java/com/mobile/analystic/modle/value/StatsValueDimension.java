package com.mobile.analystic.modle.value;

import com.mobile.common.KpiType;
import org.apache.hadoop.io.Writable;

/**
 * @ClassName StatsValueDimension
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description map和reduce阶段输出的value的类型的顶级父类
 **/
public abstract class StatsValueDimension implements Writable {
    public abstract KpiType getKpi();
}
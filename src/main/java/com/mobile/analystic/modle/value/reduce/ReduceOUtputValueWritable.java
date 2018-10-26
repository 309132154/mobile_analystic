package com.mobile.analystic.modle.value.reduce;

import com.mobile.analystic.modle.value.StatsValueDimension;
import com.mobile.common.KpiType;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.WritableUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @ClassName ReduceOUtputValueWritable
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description reduce阶段输出的value类型
 **/
public class ReduceOUtputValueWritable extends StatsValueDimension {
    private KpiType kpi;
    private MapWritable value = new MapWritable();

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        WritableUtils.writeEnum(dataOutput,kpi);
        this.value.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        WritableUtils.readEnum(dataInput,KpiType.class);
        this.value.readFields(dataInput);
    }

    @Override
    public KpiType getKpi() {
        return this.kpi;
    }

    public void setKpi(KpiType kpi) {
        this.kpi = kpi;
    }

    public MapWritable getValue() {
        return value;
    }

    public void setValue(MapWritable value) {
        this.value = value;
    }
}
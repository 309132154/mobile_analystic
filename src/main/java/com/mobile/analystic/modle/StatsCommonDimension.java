package com.mobile.analystic.modle;

import com.mobile.analystic.modle.base.BaseDimension;
import com.mobile.analystic.modle.base.DateDimension;
import com.mobile.analystic.modle.base.KpiDimension;
import com.mobile.analystic.modle.base.PlatformDimension;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * @ClassName StatsCommonDimension
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description 通用的维度累的封装(platformDimension\DateDimension\KpiDimension)
 **/
public class StatsCommonDimension extends StatsDimension{
    private DateDimension dateDimension = new DateDimension();
    private KpiDimension kpiDimension = new KpiDimension();
    private PlatformDimension platformDimension = new PlatformDimension();

    public StatsCommonDimension(){

    }

    public StatsCommonDimension(DateDimension dateDimension, KpiDimension kpiDimension, PlatformDimension platformDimension) {
        this.dateDimension = dateDimension;
        this.kpiDimension = kpiDimension;
        this.platformDimension = platformDimension;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        this.dateDimension.write(dataOutput);
        this.kpiDimension.write(dataOutput);
        this.platformDimension.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.dateDimension.readFields(dataInput);
        this.kpiDimension.readFields(dataInput);
        this.platformDimension.readFields(dataInput);
    }

    /**
     * 根据当前实例克隆一个实例
     * @param dimension
     * @return
     */
    public static StatsCommonDimension clone(StatsCommonDimension dimension){
        DateDimension dateDimension = new DateDimension(dimension.dateDimension.getYear(),
                dimension.dateDimension.getSeason(),dimension.dateDimension.getMonth(),
                dimension.dateDimension.getWeek(),dimension.dateDimension.getDay(),
                dimension.dateDimension.getType(),dimension.dateDimension.getCalendar());
        KpiDimension kpiDimension = new KpiDimension(dimension.kpiDimension.getKpiName());
        PlatformDimension platformDimension = new PlatformDimension(dimension.platformDimension.getPlatformName());
        return new StatsCommonDimension(dateDimension,kpiDimension,platformDimension);
    }

    @Override
    public int compareTo(BaseDimension o) {
        if(this == o){
            return 0;
        }

        StatsCommonDimension other = (StatsCommonDimension) o;
        int tmp = this.dateDimension.compareTo(other.dateDimension);
        if(tmp != 0){
            return tmp;
        }
        tmp = this.platformDimension.compareTo(other.platformDimension);
        if(tmp != 0){
            return tmp;
        }
        return this.kpiDimension.compareTo(other.kpiDimension);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatsCommonDimension that = (StatsCommonDimension) o;
        return Objects.equals(dateDimension, that.dateDimension) &&
                Objects.equals(kpiDimension, that.kpiDimension) &&
                Objects.equals(platformDimension, that.platformDimension);
    }

    @Override
    public int hashCode() {

        return Objects.hash(dateDimension, kpiDimension, platformDimension);
    }

    public DateDimension getDateDimension() {
        return dateDimension;
    }

    public void setDateDimension(DateDimension dateDimension) {
        this.dateDimension = dateDimension;
    }

    public KpiDimension getKpiDimension() {
        return kpiDimension;
    }

    public void setKpiDimension(KpiDimension kpiDimension) {
        this.kpiDimension = kpiDimension;
    }

    public PlatformDimension getPlatformDimension() {
        return platformDimension;
    }

    public void setPlatformDimension(PlatformDimension platformDimension) {
        this.platformDimension = platformDimension;
    }
}
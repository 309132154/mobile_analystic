package com.mobile.analystic.modle;

import com.mobile.analystic.modle.base.BaseDimension;
import com.mobile.analystic.modle.base.BrowserDimension;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * @ClassName StatsUserDimension
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description 该类可以用于用户模块和浏览器模块中的map和reduce的输出的key
 **/
public class StatsUserDimension extends StatsDimension{
    private StatsCommonDimension statsCommonDimension = new StatsCommonDimension();
    private BrowserDimension browserDimension = new BrowserDimension();

    public StatsUserDimension(){

    }

    public StatsUserDimension(StatsCommonDimension statsCommonDimension, BrowserDimension browserDimension) {
        this.statsCommonDimension = statsCommonDimension;
        this.browserDimension = browserDimension;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        this.statsCommonDimension.write(dataOutput);
        this.browserDimension.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.statsCommonDimension.readFields(dataInput);
        this.browserDimension.readFields(dataInput);
    }

    @Override
    public int compareTo(BaseDimension o) {
        if(this == o){
            return 0;
        }
        StatsUserDimension other = (StatsUserDimension) o;
        int tmp = this.statsCommonDimension.compareTo(other.statsCommonDimension);
        if(tmp != 0){
            return  tmp;
        }
        return this.browserDimension.compareTo(other.browserDimension);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatsUserDimension that = (StatsUserDimension) o;
        return Objects.equals(statsCommonDimension, that.statsCommonDimension) &&
                Objects.equals(browserDimension, that.browserDimension);
    }

    @Override
    public int hashCode() {

        return Objects.hash(statsCommonDimension, browserDimension);
    }

    public StatsCommonDimension getStatsCommonDimension() {
        return statsCommonDimension;
    }

    public void setStatsCommonDimension(StatsCommonDimension statsCommonDimension) {
        this.statsCommonDimension = statsCommonDimension;
    }

    public BrowserDimension getBrowserDimension() {
        return browserDimension;
    }

    public void setBrowserDimension(BrowserDimension browserDimension) {
        this.browserDimension = browserDimension;
    }
}
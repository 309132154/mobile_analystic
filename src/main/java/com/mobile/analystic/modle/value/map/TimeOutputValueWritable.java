package com.mobile.analystic.modle.value.map;

import com.mobile.analystic.modle.value.StatsValueDimension;
import com.mobile.common.KpiType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @ClassName TimeOutputValueWritable
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description map阶段输出的value的类型
 **/
public class TimeOutputValueWritable extends StatsValueDimension {
    private String id; //泛指，可能是uuid、mid、sid
    private long time;  //时间戳

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.id);
        dataOutput.writeLong(this.time);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.id = dataInput.readUTF();
        this.time = dataInput.readLong();
    }

    @Override
    public KpiType getKpi() {
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
package com.mobile.analystic.mr.nu;

import com.mobile.analystic.modle.StatsUserDimension;
import com.mobile.analystic.modle.value.map.TimeOutputValueWritable;
import com.mobile.analystic.modle.value.reduce.ReduceOutputValueWritable;
import org.apache.hadoop.mapreduce.Reducer;


/**
 * @ClassName NewUserRecucer
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description
 **/
public class NewUserRecucer extends Reducer<StatsUserDimension,TimeOutputValueWritable,StatsUserDimension,ReduceOutputValueWritable> {


}
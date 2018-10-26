package com.mobile.analystic.mr.nu;

import com.mobile.analystic.modle.StatsUserDimension;
import com.mobile.analystic.modle.value.map.TimeOutputValueWritable;
import com.mobile.analystic.modle.value.reduce.ReduceOUtputValueWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


/**
 * @ClassName NewUserRecucer
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description //TODO $
 **/
public class NewUserRecucer extends Reducer<StatsUserDimension,TimeOutputValueWritable,StatsUserDimension,ReduceOUtputValueWritable> {


}
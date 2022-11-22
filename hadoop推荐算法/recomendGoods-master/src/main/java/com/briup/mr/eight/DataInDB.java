package com.briup.mr.eight;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by Intellij IDEA.
 *
 * @author zhudezhong
 * @date 2021/7/29 16:51
 */
public class DataInDB {

    public static class DataInDBMapper extends Mapper<Text, NullWritable, RecommendResultBean, NullWritable> {
        @Override
        protected void map(Text key, NullWritable value, Context context) throws IOException, InterruptedException {
            String[] line = key.toString().split("\t");
            RecommendResultBean outK = new RecommendResultBean();
            outK.setNums(Double.parseDouble(line[1]));

            String[] split = line[0].split(",");
            outK.setUid(Integer.parseInt(split[0]));
            outK.setGid(Integer.parseInt(split[1]));

            context.write(outK, NullWritable.get());
        }
    }

    public static class DataInDBReducer extends Reducer<RecommendResultBean, DoubleWritable, RecommendResultBean, NullWritable> {
        @Override
        protected void reduce(RecommendResultBean key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            context.write(key, NullWritable.get());
        }
    }
}

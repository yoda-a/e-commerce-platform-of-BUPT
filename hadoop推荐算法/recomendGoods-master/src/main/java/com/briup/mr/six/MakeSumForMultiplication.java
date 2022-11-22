package com.briup.mr.six;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by Intellij IDEA.
 *
 * @author zhudezhong
 * @date 2021/7/29 0:04
 */
//第六步：对第5步计算的推荐的零散结果进行求和。
public class MakeSumForMultiplication {

    public static class MakeSumForMultiplicationMapper extends Mapper<Text, DoubleWritable, Text, DoubleWritable> {
        //MAP读入数据：10006,20007	3
        @Override
        protected void map(Text key, DoubleWritable value, Context context) throws IOException, InterruptedException {
            context.write(key, value);
        }
    }

    public static class MakeSumForMultiplicationReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

        @Override
        protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            double sum = 0;

            for (DoubleWritable value : values) {
                sum += value.get();
            }
            context.write(key, new DoubleWritable(sum));
        }
    }
}

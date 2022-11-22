package com.briup.mr.seven;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by Intellij IDEA.
 *
 * @author zhudezhong
 * @date 2021/7/29 0:19
 */
//数据去重，在推荐结果中去掉用户已购买的商品信息。
public class DuplicateDataForResult {

    //FirstMapper处理用户的购买列表数据
    public static class DuplicateDataForResultFirstMapper extends Mapper<LongWritable, Text, UserAndGoods, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //flag 为1 表示数据来源于源数据
            //flag 为0 表示数据来源于第六步结果
            String[] line = value.toString().split("\t");

            context.write(new UserAndGoods(line[0], line[1], 1), value);
        }
    }

    //SecondMapper处理第6的推荐结果数据
    public static class DuplicateDataForResultSecondMapper extends Mapper<Text, DoubleWritable, UserAndGoods, Text> {
        @Override
        protected void map(Text key, DoubleWritable value, Context context) throws IOException, InterruptedException {
            String[] line = key.toString().split(",");

            context.write(new UserAndGoods(line[0], line[1], 0), new Text(key.toString() + "\t" + value.get()));
        }
    }


    /*
        reduce期望输出的数据：10001	20004	2
     */
    public static class DuplicateDataForResultReducer extends Reducer<UserAndGoods, Text, Text, NullWritable> {
        int i = 0;
        @Override
        protected void reduce(UserAndGoods key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            Iterator<Text> iter = values.iterator();

            System.out.println((i++) + "--" + key);
            //集合的第一个元素
            Text res = iter.next();
            System.out.println(res.toString());

            //如果集合没有下一个元素，直接写出
            if (!iter.hasNext()) {
                System.out.println("有下一个元素");
                context.write(res, NullWritable.get());
            }

        }
    }
}

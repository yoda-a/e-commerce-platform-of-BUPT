package com.briup.mr.one;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * Created by Intellij IDEA.
 *
 * @author zhudezhong
 * @date 2021/7/27 14:17
 */
//step1:计算用户购买商品的列表
    //结果数据： 10001	20001,20005,20006,20007,20002
public class UserBuyGoodsList{


    //输入：10001	20001	1
    public static class UserBuyGoodsListMapper extends Mapper<LongWritable, Text, Text, Text> {
        private Text outK = new Text();
        private Text outV = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] line = value.toString().split("\t");
            //设置输出的键为用户id
            outK.set(line[0]);
            outV.set(line[1]);
            context.write(outK, outV);
        }
    }

    public static class UserBuyGoodsListReducer extends Reducer<Text, Text, Text, Text> {
        private Text outV = new Text();

        ////结果数据： 10001	20001,20005,20006,20007,20002
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            StringBuffer sb = new StringBuffer();

            for (Text value : values) {
                //拼接字符串
                sb.append(value.toString() + ",");
            }

            //将字符串最后的‘，’去掉
            sb.setLength(sb.length() - 1);

            outV.set(sb.toString());
            context.write(key, new Text(sb.toString()));
            outV.clear();
        }
    }

}

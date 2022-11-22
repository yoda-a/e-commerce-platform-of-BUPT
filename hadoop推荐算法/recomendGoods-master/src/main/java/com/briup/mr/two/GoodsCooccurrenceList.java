package com.briup.mr.two;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by Intellij IDEA.
 *
 * @author zhudezhong
 * @date 2021/7/27 15:22
 */
//计算商品的共现关系   即两两商品出现的组合有哪些     期望结果：20001	20001
//不需要reduce程序
public class GoodsCooccurrenceList {
    //使用sequencefileinputformat读取数据，读入的数据自动基于键和值分割
    public static class GoodsCooccurrenceListMapper extends Mapper<Text, Text, Text, NullWritable> {
        private StringBuffer sb = new StringBuffer();
        private Text outK = new Text();

        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            String[] line = value.toString().split(",");

            //每个商品id两两组合
            for (String s : line) {
                for (String s1 : line) {
                    sb.append(s).append("\t").append(s1);

                    outK.set(sb.toString());
                    context.write(outK, NullWritable.get());
                    sb.setLength(0);
                    outK.clear();
                }
            }
        }
    }
}

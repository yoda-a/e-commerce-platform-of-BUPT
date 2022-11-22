package com.briup.mr.four;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @author zhudezhong
 * @date 2021/7/27 20:25
 */

//计算用户的购买向量
public class UserBuyGoodsVector {
    /*
        输入数据：10001	20001	1   读取源文件为源数据
     */
    public static class UserBuyGoodsVectorMapper extends Mapper<LongWritable, Text, Text, Text> {
        /*
            商品id为键，用户id为值
         */
        private Text outK = new Text();
        private Text outV = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] line = value.toString().split("\t");

            outK.set(line[1]);
            outV.set(line[0]);

            context.write(outK, outV);
        }
    }

    public static class UserBuyGoodsVectorReducer extends Reducer<Text, Text, Text, Text> {
        /*
            输入为：20001   [10001,10002.。。]
         */
        private Text outV = new Text();
        private Map<String, Integer> map = new HashMap<>();
        private StringBuffer sb = new StringBuffer();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            /*
                map端输出的key为商品id，值为用户id的集合
                结果：
                20001	10001:1,10004:1,10005:1
             */

            for (Text value : values) {
                if (map.containsKey(value.toString())) {
                    map.put(value.toString(), map.get(value.toString()) + 1);
                } else {
                    map.put(value.toString(), 1);
                }
            }

            for (Map.Entry<String, Integer> en : map.entrySet()) {
                sb.append(en.getKey()).append(":").append(en.getValue()).append(",");
            }
            sb.setLength(sb.length()-1);
            outV.set(sb.toString());
            context.write(key,outV);

            //重置数据
            sb.setLength(0);
            map.clear();
            outV.clear();

        }
    }
}

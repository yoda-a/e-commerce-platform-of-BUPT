package com.briup.mr.five;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

/**
 * Created by Intellij IDEA.
 *
 * @author zhudezhong
 * @date 2021/7/27 21:40
 */

/*
    step5:  商品共现矩阵乘以用户购买向量，形成临时的推荐结果。
 */
public class MultiplyGoodsMatrixAndUserVector {

    /*
    输入数据:第3步的结果
        物品共现矩阵： 20001	20005:2,20002:2,20001:3,20007:1,20006:2
     */

    public static class MultiplyGoodsMatrixAndUserVectorFirstMapper extends Mapper<Text, Text, GoodsBean, Text> {
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            //map输出数据为： GoodsBean(20001，1)  20005:2,20002:2,20001:3,20007:1,20006:2
            context.write(new GoodsBean(key.toString(), 1), value);
        }
    }

    /*
    输入数据：
        用户购买向量： 20001	10001:1,10004:1,10005:1
     */
    public static class MultiplyGoodsMatrixAndUserVectorSecondMapper extends Mapper<Text, Text, GoodsBean, Text> {
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            //map输出数据为：GoodsBean(20001，0)    10001:1,10004:1,10005:1
            context.write(new GoodsBean(key.toString(), 0), value);
        }
    }

    /*
    期望输出数据: 10001,20001	2
     */
    public static class MultiplyGoodsMatrixAndUserVectorReducer extends Reducer<GoodsBean, Text, Text, DoubleWritable> {
        /*
            进入reduce的数据为：
                GoodsBean(20001，1)GoodsBean(20001，0)    20005:2,20002:2,20001:3,20007:1,20006:2 || 10001:1,10004:1,10005:1
         */
        @Override
        protected void reduce(GoodsBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            Iterator<Text> iter = values.iterator();

            //拿到商品项：20005:2,20002:2,20001:3,20007:1,20006:2
            String[] goods = iter.next().toString().split(",");


            while (iter.hasNext()) {
                //拿到用户购买向量：10001:1,10004:1,10005:1
                String[] users = iter.next().toString().split(",");
//                System.out.println(Arrays.toString(users));
                for (String user : users) {
                    String[] uid_nums = user.split(":");

                    for (String good : goods) {
                        String[] gid_nums = good.split(":");
                        //sb作为key输出
                        StringBuffer sb = new StringBuffer();

                        sb.append(uid_nums[0]).append(",").append(gid_nums[0]);

                        context.write(new Text(sb.toString()), new DoubleWritable(Double.parseDouble(uid_nums[1]) * Double.parseDouble(gid_nums[1])));

                        sb.setLength(0);
                    }
                }
            }

        }
    }

}

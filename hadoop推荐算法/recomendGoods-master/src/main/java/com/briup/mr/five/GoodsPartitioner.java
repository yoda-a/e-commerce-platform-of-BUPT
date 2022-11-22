package com.briup.mr.five;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.HashPartitioner;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Created by Intellij IDEA.
 *
 * @author zhudezhong
 * @date 2021/7/28 20:07
 */
public class GoodsPartitioner extends Partitioner<GoodsBean, Text> {

    @Override
    public int getPartition(GoodsBean goodsBean, Text text, int numPartitions) {
        return Math.abs(Integer.parseInt(goodsBean.getG_id()) * 127) % numPartitions;
    }
}

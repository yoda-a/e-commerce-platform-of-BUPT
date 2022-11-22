package com.briup.mr.five;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * Created by Intellij IDEA.
 *
 * @author zhudezhong
 * @date 2021/7/28 20:14
 */
public class GoodsGroup extends WritableComparator {
    public GoodsGroup() {
        super(GoodsBean.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        //基于商品id分组，id相同的分为一组
        GoodsBean o = (GoodsBean) a;
        GoodsBean o1 = (GoodsBean) b;

        return o.getG_id().compareTo(o1.getG_id());
    }
}

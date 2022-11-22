package com.briup.mr.five;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by Intellij IDEA.
 *
 * @author zhudezhong
 * @date 2021/7/28 9:21
 */
public class GoodsBean implements WritableComparable<GoodsBean> {
    private String g_id;    //商品id
    //flag为1表示数据来自商品共现次数（第3步结果）
    //flag为0表示数据来自用户购买向量（第四步结果）
    private int flag;

    public GoodsBean() {
    }

    public GoodsBean(String g_id, int flag) {
        this.g_id = g_id;
        this.flag = flag;
    }

    public String getG_id() {
        return g_id;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public int compareTo(GoodsBean o) {
        int n = this.g_id.compareTo(o.g_id);
        if (n != 0) {
            return n;
        } else {
            //将商品共现表的数据放在前面
            return -(this.flag - o.flag);
        }
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(g_id);
        dataOutput.writeInt(flag);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.g_id = dataInput.readUTF();
        this.flag = dataInput.readInt();
    }
}

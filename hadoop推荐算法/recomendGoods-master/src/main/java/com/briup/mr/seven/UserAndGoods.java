package com.briup.mr.seven;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by Intellij IDEA.
 *
 * @author zhudezhong
 * @date 2021/7/29 0:24
 */
public class UserAndGoods implements WritableComparable<UserAndGoods> {
    private String userId;
    private String goodsId;
    //flag 为1 表示数据来源于源数据
    //flag 为0 表示数据来源于第六步结果
    private int flag;

    public UserAndGoods() {
    }

    public UserAndGoods(String userId, String goodsId, int flag) {
        this.userId = userId;
        this.goodsId = goodsId;
        this.flag = flag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public int compareTo(UserAndGoods o) {

        int i = this.getUserId().compareTo(o.getUserId());
        //当用户i不相同时
        if (i != 0) {
            return i;
        } else return this.getGoodsId().compareTo(o.getGoodsId());
    }

    @Override
    public String toString() {
        return "UserAndGoods{" +
                "userId='" + userId + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", flag=" + flag +
                '}';
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(userId);
        dataOutput.writeUTF(goodsId);
        dataOutput.writeInt(flag);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.userId = dataInput.readUTF();
        this.goodsId = dataInput.readUTF();
        this.flag = dataInput.readInt();
    }
}

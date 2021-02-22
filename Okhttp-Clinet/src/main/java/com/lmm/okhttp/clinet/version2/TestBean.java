package com.lmm.okhttp.clinet.version2;

import com.google.gson.annotations.SerializedName;

/**
 * desc   :
 * author : fei
 * date   : 2021/02/22
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class TestBean {


    /**
     * userId : 1
     * id : 1
     * title : delectus aut autem
     * completed : false
     */

    @SerializedName("userId")
    private Integer userId;
    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private String title;
    @SerializedName("completed")
    private Boolean completed;

    @Override
    public String toString() {
        return "TestBean{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                '}';
    }
}
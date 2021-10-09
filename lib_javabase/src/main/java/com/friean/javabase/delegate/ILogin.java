package com.friean.javabase.delegate;

import com.friean.javabase.delegate.annotations.Field;
import com.friean.javabase.delegate.annotations.Get;
import com.friean.javabase.delegate.annotations.PATH;

/**
 * desc   :
 * author : fei
 * date   : 2021/03/05
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public interface ILogin {
    void login();

    @Get("/loginOut")
    String loginOut(@PATH("name") String name, @Field("age") String age);
}
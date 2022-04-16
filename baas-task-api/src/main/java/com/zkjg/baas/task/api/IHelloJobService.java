package com.zkjg.baas.task.api;

import com.zkjg.baas.base.bean.BaseResult;

/**
 * @author wylu
 * @date 2022/4/16 上午 09:17
 */
public interface IHelloJobService {

    /**
     * 测试方法
     *
     * @param language 语言
     * @param author 作者
     * @param content 内容
     * @return
     */
    BaseResult<String> sayHelloJob(String language, String author, String content);
}

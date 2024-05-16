package com.jun.project.provider;

import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author 27164
 * @version 1.0
 * @description: TODO
 * @date 2024/5/16 14:42
 */
@DubboService
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {

        return "hello"+name;
    }

    @Override
    public String sayHello2(String name) {
        return "你真帅"+name;
    }
}

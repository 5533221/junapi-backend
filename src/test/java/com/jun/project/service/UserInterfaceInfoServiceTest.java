package com.jun.project.service;

import com.junapicommon.service.InnerUserInterfaceInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserInterfaceInfoServiceTest {

    @Autowired
    private InnerUserInterfaceInfoService userInterfaceInfoService;

    @Test
    void invokeAddCount() {


        boolean b = userInterfaceInfoService.invokeAddCount(1L, 1L);
        System.out.println(b?"成":"失");
    }
}
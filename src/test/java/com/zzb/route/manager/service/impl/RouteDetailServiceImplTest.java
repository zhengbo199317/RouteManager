package com.zzb.route.manager.service.impl;

import com.zzb.route.manager.dataObject.RouteDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

@SpringBootTest
class RouteDetailServiceImplTest {
    @Autowired
    private RouteDetailServiceImpl service;

    @Test
    void findAll() {
        Page<RouteDetail> all = service.findAll(1, 10);
        System.out.println(all);
    }
}
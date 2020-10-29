package com.zzb.route.manager.utils;

import com.google.gson.Gson;
import com.zzb.route.manager.dataObject.RouteDetail;
import com.zzb.route.manager.service.RouteDetailService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/*
服务器连接测试
 */
@Component
public class DelaylUtils {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RouteDetailService routeDetailService;
    Gson gson = new Gson();

    @Async
    public void delay(String routeName) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        RouteDetail routeDetail=null;
        String routeStr = ops.get(routeName);
        if (routeStr==null){
            routeDetail=routeDetailService.findByRouteName(routeName);
        }else{
            routeDetail = gson.fromJson(routeStr, RouteDetail.class);
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        long dif = 0;
        try {
//            System.out.println(Thread.currentThread().getName()+"开始了");
            HttpGet httpGet = new HttpGet(routeDetail.getRouteUrl());
            long start = System.currentTimeMillis();
            CloseableHttpResponse execute = httpClient.execute(httpGet);
            long end = System.currentTimeMillis();
            dif = end - start;
            routeDetail.setRouteDelay(String.valueOf(dif)).setRouteStatus("服务正常");
            ops.set(routeDetail.getRouteName(), gson.toJson(routeDetail));
        } catch (Exception e) {
            //连接失败
            routeDetail.setRouteDelay("-1").setRouteStatus("服务异常");
            ops.set(routeDetail.getRouteName(), gson.toJson(routeDetail));
        }
//        System.out.println(Thread.currentThread().getName()+"结束了");
    }
}

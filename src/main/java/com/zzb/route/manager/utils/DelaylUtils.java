package com.zzb.route.manager.utils;

import com.google.gson.Gson;
import com.zzb.route.manager.dataObject.RouteDetail;
import com.zzb.route.manager.service.RouteDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;

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
        long dif = 0;
        try {
            URL url=new URL(routeDetail.getRouteUrl());
            long start = System.currentTimeMillis();
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            con.setConnectTimeout(3000);
            if (con.getResponseCode()>200){
                routeDetail.setRouteDelay("-1").setRouteStatus("服务异常");
                ops.set(routeDetail.getRouteName(), gson.toJson(routeDetail));
            }else{
                long end = System.currentTimeMillis();
                dif = end - start;
                routeDetail.setRouteDelay(String.valueOf(dif)+"ms").setRouteStatus("服务正常");
                ops.set(routeDetail.getRouteName(), gson.toJson(routeDetail));
            }
        } catch (Exception e) {
            //连接失败
            routeDetail.setRouteDelay("-1").setRouteStatus("服务异常");
            ops.set(routeDetail.getRouteName(), gson.toJson(routeDetail));
        }
    }

    public static void main(String[] args) {
        try {
            long start = System.currentTimeMillis();
            URL url=new URL("http://192.168.0.183:8080/jsaas");
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(3000);
            if (connection.getResponseCode()>200){
                System.out.println(false);
            }else{
                System.out.println(true);
            }
            long end = System.currentTimeMillis();
            System.out.println(end-start);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

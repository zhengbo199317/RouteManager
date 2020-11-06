package com.zzb.route.manager.conf;

import com.google.gson.Gson;
import com.zzb.route.manager.dataObject.RouteDetail;
import com.zzb.route.manager.utils.DelaylUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Configuration
public class ScheduleConf {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private DelaylUtils delaylUtils;
    Gson gson = new Gson();

    /*
    间隔8s定时器更新服务器状态
     */
    @Scheduled(cron = "8,16,24,32,40,58 * * * * ? ")
    public void refreshRouteStatus() {
        List<String>  routeNames = new ArrayList<>();
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        //redis分布式锁
        Long refreshRouteStatus = ops.increment("refreshRouteStatus");
        if (refreshRouteStatus>1){
            System.out.println("有别的线程在执行");
            return;
        }
        try {
            String routeStr = ops.get("routes");
            if (routeStr == null) {
                //修改为没有就没有,去除mysql化
                //结束锁
                ops.set("refreshRouteStatus","0",1, TimeUnit.MILLISECONDS);
                return;
            } else {
                routeNames=gson.fromJson(routeStr, List.class);
            }
            for (String routeName:routeNames){
                delaylUtils.delay(routeName);
            }
        }catch (Exception e){
            //结束锁
            ops.set("refreshRouteStatus","0",1, TimeUnit.MILLISECONDS);
            return;
        }
        //结束锁
        ops.set("refreshRouteStatus","0",1, TimeUnit.MILLISECONDS);
    }
    /*
    间隔8s检测服务状态
     */
    @Scheduled(cron = "6,14,22,30,48,56 * * * * ? ")
    public void refreshNginxValue() {
        List<String>  routeNames = new ArrayList<>();
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        //redis分布式锁
        Long refreshNginxValue = ops.increment("refreshNginxValue");
        if (refreshNginxValue>1){
            System.out.println("有别的线程在执行");
            return;
        }
        try {
            String routeStr = ops.get("routes");
            if (routeStr == null) {
                //修改为没有就没有,去除mysql化
                //结束锁
                ops.set("refreshNginxValue","0",1, TimeUnit.MILLISECONDS);
                return;
            } else {
                routeNames=gson.fromJson(routeStr, List.class);
            }
            List<RouteDetail> routeDetails=new ArrayList<>();
            for (String routeName:routeNames){
                String routeDetail = ops.get(routeName);
                routeDetails.add(gson.fromJson(routeDetail, RouteDetail.class));
            }
            List<String> serverNames = routeDetails.stream()
                    .map(e -> e.getServerName()).distinct()
                    .filter(e->(!StringUtils.isEmpty(e)))
                    .collect(Collectors.toList());
            for (String serverName:serverNames) {
                String serverUrl = ops.get(serverName);
                //第一次初始化
                if (StringUtils.isEmpty(serverUrl)) {
                    List<RouteDetail> rds = routeDetails.stream().filter(e -> e.getServerName().equals(serverName))
                            .collect(Collectors.toList());
                    ops.set(serverName, rds.get(0).getServerUrl());
                    //结束锁
                    ops.set("refreshNginxValue","0",1, TimeUnit.MILLISECONDS);
                    return;
                }
                RouteDetail veryRd = routeDetails.stream()
                        .filter(e -> e.getServerUrl().equals(serverUrl))
                        .collect(Collectors.toList()).get(0);
                //如果服务异常
                if (!veryRd.getRouteStatus().equals("服务正常")) {
                    List<RouteDetail> workRd = routeDetails.stream()
                            .filter(e -> e.getServerName().equals(serverName))
                            .filter(e -> (!e.getServerUrl().equals(serverUrl)))
                            .collect(Collectors.toList());
                    if (workRd.size() > 0) {
                        ops.set(serverName, workRd.get(0).getServerUrl());
                    }
                }
            }
        }catch (Exception e){
            //结束锁
            ops.set("refreshNginxValue","0",1, TimeUnit.MILLISECONDS);
            return;
        }
        //结束锁
        ops.set("refreshNginxValue","0",1, TimeUnit.MILLISECONDS);
    }

    /*
    定时器更新服务器状态
     */
//    @Scheduled(cron = "00 * * * * ?")
//    public void refreshRoute() {
//        List<String>  routeNames = new ArrayList<>();
//        ValueOperations<String, String> ops = redisTemplate.opsForValue();
//        String routeStr = ops.get("routes");
//        if (routeStr == null) {
//            return;
//        } else {
//            routeNames=gson.fromJson(routeStr, List.class);
//            List<RouteDetail> routeDetails=new ArrayList<>();
//            routeNames.stream().forEach(e->routeDetails.add(gson.fromJson(ops.get(e),RouteDetail.class)));
//            routeDetailService.save(routeDetails);
//        }
//    }
}

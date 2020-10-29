package com.zzb.route.manager.conf;

import com.google.gson.Gson;
import com.zzb.route.manager.dataObject.RouteDetail;
import com.zzb.route.manager.service.RouteDetailService;
import com.zzb.route.manager.utils.DelaylUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ScheduleConf {
    @Autowired
    private RouteDetailService routeDetailService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private DelaylUtils delaylUtils;
    Gson gson = new Gson();

    /*
    定时器更新服务器状态
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void refreshRouteStatus() {
        List<String>  routeNames = new ArrayList<>();
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String routeStr = ops.get("routes");
        if (routeStr == null) {
            List<RouteDetail> routeDetails = routeDetailService.findAll();
            routeNames = routeDetails.stream().map(e -> e.getRouteName()).collect(Collectors.toList());
            ops.set("routes", gson.toJson(routeNames));
        } else {
            routeNames=gson.fromJson(routeStr, List.class);
        }
        for (String routeName:routeNames){
            delaylUtils.delay(routeName);
        }
    }

    /*
    定时器更新服务器状态
     */
    @Scheduled(cron = "00 10 * * * ?")
    public void refreshRoute() {
        List<String>  routeNames = new ArrayList<>();
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String routeStr = ops.get("routes");
        if (routeStr == null) {
            return;
        } else {
            routeNames=gson.fromJson(routeStr, List.class);
            List<RouteDetail> routeDetails=new ArrayList<>();
            routeNames.stream().forEach(e->routeDetails.add(gson.fromJson(ops.get(e),RouteDetail.class)));
            routeDetailService.save(routeDetails);
        }
    }
}

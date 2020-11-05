package com.zzb.route.manager.controller.portController;

import com.google.gson.Gson;
import com.zzb.route.manager.dataObject.RouteDetail;
import com.zzb.route.manager.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "路由管理")
@RestController
@RequestMapping(value = "/routes")
public class RouteController extends ResultVo {
    @Autowired
    private StringRedisTemplate redisTemplate;
    Gson gson=new Gson();

    @ApiOperation(value = "获取所有监控网址和状态")
    @RequestMapping(value = "/routesGet", method = RequestMethod.GET)
    public ResultVo routesGet() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String routesName = ops.get("routes");
        List<String> names = gson.fromJson(routesName, List.class);
        List<RouteDetail> list=new ArrayList<>();
        names.stream().forEach(e->list.add(gson.fromJson(ops.get(e),RouteDetail.class)));
        ResultVo resultVo = success(list).setMsg("").setCode(0).setCount(names.size());
        return resultVo;
    }

    @ApiOperation(value = "获取概览数据")
    @RequestMapping(value = "/managerGet", method = RequestMethod.POST)
    public ResultVo managerGet(){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String routesName = ops.get("routes");
        List<String> names = gson.fromJson(routesName, List.class);
        List<RouteDetail> list=new ArrayList<>();
        names.stream().forEach(e->list.add(gson.fromJson(ops.get(e),RouteDetail.class)));
        List<RouteDetail> norMals = list.stream().filter(e -> e.getRouteStatus().equals("服务正常")).collect(Collectors.toList());
        List<Integer> countList=new ArrayList<>();
        countList.add(list.size()-norMals.size());
        countList.add(norMals.size());
        countList.add(list.size());
        return success(countList);
    }

    @ApiOperation(value = "添加服务器")
    @RequestMapping(value = "/routePost", method = RequestMethod.POST)
    public ResultVo routePost(String url, String name) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return success();
    }

    @ApiOperation(value = "监控删除")
    @RequestMapping(value = "/routeDelete", method = RequestMethod.POST)
    public ResultVo routeDelete(String name) {
        return success();
    }

    @ApiOperation(value = "监控添加")
    @RequestMapping(value = "/routesPut",method = RequestMethod.POST)
    public ResultVo routesPut(String routeName,String routeUrl,String serverName
            ,String workerName,String workerPhone,String serverUrl){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String routesName = ops.get("routes");
        String serverNames = ops.get("serverNames");
        List<String> names=new ArrayList<>();
        if (routesName!=null){
            names = gson.fromJson(routesName, List.class);
            if (serverNames!=null){
                List<String> serverNameList = gson.fromJson(serverNames, List.class);
                if (serverNameList.contains(routeName)){
                    return faild("服务器名和已存在的调用服务名冲突");
                }
            }
            if (names.contains(routeName)){
                return faild("服务器名已存在");
            }
        }
        names.add(routeName);
        ops.set("routes",gson.toJson(names));
        RouteDetail routeDetail = new RouteDetail().setWorkerName(workerName)
                .setRouteName(routeName).setWorkerPhone(workerPhone)
                .setServerName(serverName).setRouteUrl(routeUrl)
                .setServerUrl(serverUrl);
        ops.set(routeName,gson.toJson(routeDetail));
        return success();
    }
}

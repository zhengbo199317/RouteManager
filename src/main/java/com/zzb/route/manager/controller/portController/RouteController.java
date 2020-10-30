package com.zzb.route.manager.controller.portController;

import com.google.gson.Gson;
import com.zzb.route.manager.dataObject.RouteDetail;
import com.zzb.route.manager.service.RouteDetailService;
import com.zzb.route.manager.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "路由管理")
@RestController
@RequestMapping(value = "/routes")
public class RouteController extends ResultVo {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RouteDetailService routeDetailService;
    @Autowired
    private EntityManager entityManager;
    Gson gson=new Gson();

    @ApiOperation(value = "获取所有路由和路由状态")
    @RequestMapping(value = "/routesGet", method = RequestMethod.GET)
    public ResultVo routesGet(Integer page, Integer limit) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
//        Page<RouteDetail> routeDetails = routeDetailService.findAll(page - 1, limit);
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

        return success();
    }

    @ApiOperation(value = "路由删除")
    @RequestMapping(value = "/routeDelete", method = RequestMethod.POST)
    public ResultVo routeDelete(String name) {

        return success();
    }

    @ApiOperation(value = "路由添加")
    @RequestMapping(value = "/routesPut",method = RequestMethod.POST)
    public ResultVo routesPut(String routeName,String routeUrl,String serverName
            ,String workerName,String workerPhone){
        try {
            routeDetailService.save(new RouteDetail().setRouteName(routeName).setRouteUrl(routeUrl)
                    .setWorkerName(workerName).setWorkerPhone(workerPhone).setServerName(serverName));
        }catch (Exception e){
            if (e.getMessage().contains("constraint")){
                return faild("已存在");
            }
        }
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("routesRefresh","1");
        return success();
    }
}

package com.zzb.route.manager.controller.portController;

import com.google.gson.Gson;
import com.zzb.route.manager.dataObject.RouteDetail;
import com.zzb.route.manager.service.RouteDetailService;
import com.zzb.route.manager.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
    public ResultVo managerGet() throws SQLException {
        Connection connection = null;
        SessionImplementor session = null;
        List<String> list = new ArrayList<>();
        try {
            session = entityManager.unwrap(SessionImplementor.class);
            connection = session.connection();
            String sql = "select count(id) from routedetail where routeStatus ='服务异常'";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                list.add(rs.getString(1));
            }
            sql = "select count(id) from routedetail where routeStatus ='服务正常'";
            ResultSet rs2 = statement.executeQuery(sql);
            while (rs2.next()){
                list.add(rs2.getString(1));
            }
            sql="select count(id) from routedetail";
            ResultSet rs3 = statement.executeQuery(sql);
            while (rs3.next()){
                list.add(rs3.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
            session.close();
        }
        return success(list);
    }

    @ApiOperation(value = "添加路由")
    @RequestMapping(value = "/routePost", method = RequestMethod.POST)
    public ResultVo routePost(String url, String name) {

        return success();
    }

    @ApiOperation(value = "路由删除")
    @RequestMapping(value = "/routeDelete", method = RequestMethod.POST)
    public ResultVo routeDelete(String name) {

        return success();
    }
}

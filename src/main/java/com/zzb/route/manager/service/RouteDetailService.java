package com.zzb.route.manager.service;

import com.zzb.route.manager.dataObject.RouteDetail;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RouteDetailService {
    Page<RouteDetail> findAll(Integer page, Integer limit);
    Page<RouteDetail> findByServerName(String serverName,Integer page,Integer limit);
    List<RouteDetail> findAll();
    RouteDetail findByRouteName(String routeName);
    void save(RouteDetail routeDetail);
    void delete(Integer id);
    void save(List<RouteDetail> routeDetails);
}

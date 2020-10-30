package com.zzb.route.manager.repository;


import com.zzb.route.manager.dataObject.RouteDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteDetailRepository extends JpaRepository<RouteDetail,Integer> {
    Page<RouteDetail> findAllByServerName(String serverName, Pageable pageable);
    RouteDetail findByRouteName(String routeName);
}

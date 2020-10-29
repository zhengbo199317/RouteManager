package com.zzb.route.manager.repository;

import com.zzb.route.manager.dataObject.RouteField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteFieldRepository extends JpaRepository<RouteField,Integer> {
    List<RouteField> findByRouteName(String routeName);
}

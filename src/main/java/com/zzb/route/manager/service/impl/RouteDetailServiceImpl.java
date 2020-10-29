package com.zzb.route.manager.service.impl;

import com.zzb.route.manager.dataObject.RouteDetail;
import com.zzb.route.manager.repository.RouteDetailRepository;
import com.zzb.route.manager.service.RouteDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RouteDetailServiceImpl implements RouteDetailService {
    @Autowired
    private RouteDetailRepository repository;

    @Override
    public Page<RouteDetail> findAll(Integer page, Integer limit) {
        Pageable pageable= PageRequest.of(page,limit);
        Page<RouteDetail> detailPage = repository.findAll(pageable);
        return detailPage;
    }

    @Override
    public Page<RouteDetail> findByServerName(String serverName, Integer page, Integer limit) {
        Pageable pageable=PageRequest.of(page,limit);
        Page<RouteDetail> detailPage = repository.findAllByServerName(serverName, pageable);
        return detailPage;
    }

    @Override
    public void save(RouteDetail routeDetail) {
        repository.save(routeDetail);
    }

    @Override
    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public List<RouteDetail> findAll() {
        return repository.findAll();
    }

    @Override
    public RouteDetail findByRouteName(String routeName) {
        return repository.findByRouteName(routeName);
    }

    @Override
    public void save(List<RouteDetail> routeDetails) {
        repository.saveAll(routeDetails);
    }
}

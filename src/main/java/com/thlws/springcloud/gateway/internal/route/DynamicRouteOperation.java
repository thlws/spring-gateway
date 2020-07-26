package com.thlws.springcloud.gateway.internal.route;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thlws.springcloud.gateway.internal.core.model.Route;
import com.thlws.springcloud.gateway.internal.core.service.RouteService;
import com.thlws.springcloud.gateway.internal.util.RouteUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author HanleyTang 2020/7/26
 */
@Service
public class DynamicRouteOperation implements ApplicationEventPublisherAware, CommandLineRunner {

    @Resource
    private RouteService routeService;

    @Resource
    private MySqlRouteDefinitionRepository mySqlRouteDefinitionRepository;

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @Override
    public void run(String... args) throws Exception {
        mySqlRouteDefinitionRepository.getRouteDefinitions();
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void insert(Route route) {
        RouteDefinition routeDefinition = RouteUtil.buildRouteDefinition(route);
        mySqlRouteDefinitionRepository.save(Mono.just(routeDefinition)).subscribe();
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void update(Route route) {
        RouteDefinition routeDefinition = RouteUtil.buildRouteDefinition(route);
        mySqlRouteDefinitionRepository.delete(Mono.just(routeDefinition.getId()));
        mySqlRouteDefinitionRepository.save(Mono.just(routeDefinition)).subscribe();
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public Mono<ResponseEntity<Object>> delete(String id) {
        return mySqlRouteDefinitionRepository.delete(Mono.just(id))
                .then(Mono.defer(() -> Mono.just(ResponseEntity.ok().build())))
                .onErrorResume(t -> t instanceof NotFoundException,
                        t -> Mono.just(ResponseEntity.notFound().build()));
    }

    public IPage<Route> list(int pageNo,int pageSize){
        return routeService.page(new Page<>(pageNo, pageSize));
    }

    public Route detail(Long id){
        return routeService.getById(id);
    }
}

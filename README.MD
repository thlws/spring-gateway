#### 参考资料
- [https://www.haoyizebo.com/posts/1962f450/](https://www.haoyizebo.com/posts/1962f450/)
- [https://docs.spring.io/spring-cloud-gateway/docs/2.2.4.RELEASE/reference/html/](https://docs.spring.io/spring-cloud-gateway/docs/2.2.4.RELEASE/reference/html/)

#### VM options
```
-Dnacos.namespace=dev
-Dnacos.addr=localhost:8848
```

#### 网关限流
> 支持多维度限流(API,HOST,USER)
##### 限流存储
```
#存放需要限流的的HOST
redisTemplate.opsForSet().add("limiter:config:host","www.baidu.com","www.taobao.com")

#存放HOST对应的限流值
Map<String, String> map = new HashMap();
map.put("www.baiud.com", 10);
map.put("www.taobo.com", 6);
map.put("www.qq.com", 16);
redisTemplate.opsForHash().putAll("limiter:config:host",map)

#获取host对应的限流配置
redisTemplate.opsForHash().get("limiter:config:host", "www.baidu.com")
```

#### 鉴权存储
```
AuthConfig config = AuthConfig.builder()
        .path("/api/user/{name}")
        .authHttpMethod("GET")
        .build();
reactiveRedisTemplate.opsForSet().add(Const.Key.AUTH_API,config);
```

#### Swagger
- [http://localhost:8666/swagger-ui/](http://localhost:8666/swagger-ui/)
##### 查看路由
- [http://localhost:8666/actuator/gateway/routes](http://localhost:8666/actuator/gateway/routes)

#### Log4j2
- [https://www.hangge.com/blog/cache/detail_2856.html](https://www.hangge.com/blog/cache/detail_2856.html)
- [https://juejin.im/post/6844904113352736782](https://juejin.im/post/6844904113352736782)
- [https://blog.csdn.net/Leon_Jinhai_Sun/article/details/105962844](https://blog.csdn.net/Leon_Jinhai_Sun/article/details/105962844)
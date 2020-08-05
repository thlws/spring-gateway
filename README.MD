# VM options
```
-Dcsp.sentinel.app.type=1
-Dnacos.addr=localhost:8848
-Dnacos.namespace=dev
-Dcsp.sentinel.dashboard.server=127.0.0.1:8080
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

#### 动态网关
##### 参数封装
> 前端传参
```
1.当 predicates ，filters 中的 args 参数无key时，默认生成 _genkey_0
2.当 predicates ，filters 中的 args 参数由多个KV值时,key.数字区别
```
![](./docs/route_params.png)
``` json
{
	"routeId":"user",
	"routeUri":"lb://user",
	"routeOrder":0,
	"predicates": "[{\"name\":\"Path\",\"args\":{\"_genkey_0\":\"/user/**\"}}]"
}
```
##### 查看路由
- [http://localhost:8666/actuator/gateway/routes](http://localhost:8666/actuator/gateway/routes)
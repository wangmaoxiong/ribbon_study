package wmx.com.eurekaclient_cat.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;

@RestController
public class SystemController {

    //获取容器中创建好的 RestTemplate 实例
    @Resource
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    /**
     * localhost:9394/loadBalancerClient
     *
     * @return
     */
    @GetMapping("loadBalancerClient")
    public String testLoadBalancerClient() {
        //choose(String serviceId)：服务id，没有脱离 Eureka 时，这里通常就是对方服务名称，即 spring.application 属性值
        //当 Ribbon 脱离 Eureka 时，服务id 就是与自己的配置文件 xxx.ribbon.listOfServers 中的 xxx 保持一致
        ServiceInstance serviceInstance = loadBalancerClient.choose("EUREKA-CLIENT-FOOD");
        String host = serviceInstance.getHost();
        int port = serviceInstance.getPort();
        String instanceId = serviceInstance.getInstanceId();
        String serviceId = serviceInstance.getServiceId();
        URI uri = serviceInstance.getUri();
        URI storesUri = URI.create(String.format("http://%s:%s", serviceInstance.getHost(), serviceInstance.getPort()));

        System.out.println("host：" + host);
        System.out.println("port：" + port);
        System.out.println("instanceId：" + instanceId);
        System.out.println("serviceId：" + serviceId);
        System.out.println("uri：" + uri);
        System.out.println("storesUri：" + storesUri);

        return "";
    }

    //通过 id 获取猫咪信息。这里只是简单的模拟，并不是操作数据库
    //请求地址：http://localhost:9394/getCatById?id=110
    @GetMapping("getCatById")
    public String getCatById(String id) throws IOException {
        //猫咪的基本信息，这里直接设置
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        ObjectNode objectNode = nodeFactory.objectNode();
        objectNode.put("id", id);
        objectNode.put("color", "white");
        objectNode.put("age", 0.2);

        //猫咪的食谱/菜谱信息则调用 eurekaclient_food 微服务进行获取。
        //未使用负载均衡时的地址：http://localhost:9395/getHunanCuisine。使用负载均衡后，其中的 ip:port 必须使用微服务名称代替
        //EUREKA-CLIENT-FOOD 是在注册中心注册好的微服务名称(不是节点名称)，也就是微服务配置文件中使用 spring.application.name 配置的名称
        String foodMenu = restTemplate.getForObject("http://EUREKA-CLIENT-FOOD/food/getHunanCuisine", String.class);
        if (!StringUtils.isEmpty(foodMenu)) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(foodMenu);//先将 json 字符串专户 json 节点对象
            objectNode.putPOJO("menu", jsonNode);//对象节点插入子节点
        }
        return objectNode.toString();
    }
}

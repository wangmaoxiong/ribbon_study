package wmx.com.eurekaclient_food.controller;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜谱
 *
 * @author wangmaoxiong
 */
@RestController
public class Cuisine {

    @Value("${server.port}")
    private Integer server_port;

    /**
     * 获取湘菜菜谱数据。访问地址：http://localhost:9395/getHunanCuisine
     *
     * @return
     */
    @GetMapping("getHunanCuisine")
    public String getHunanCuisine() {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        //为了后面多个节点集群，负载均衡访问的时候，能分得清请求的到底是谁，这里特意返回应用的端口号
        ArrayNode arrayNode = nodeFactory.arrayNode().add(server_port)
                .add("辣椒炒肉")
                .add("剁椒鱼头")
                .add("蚂蚁上树")
                .add("麻婆豆腐");
        return arrayNode.toString();
    }
}

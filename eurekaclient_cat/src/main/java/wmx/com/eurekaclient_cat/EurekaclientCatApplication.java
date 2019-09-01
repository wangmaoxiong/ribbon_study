package wmx.com.eurekaclient_cat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

//Eureka 客户端在启动类上可以不加 @EnableEurekaClient 注解
@SpringBootApplication
public class EurekaclientCatApplication {

    /**
     * @SpringBootApplication 注解自己依赖了 @Configuration 注解，所以可以直接在启动类上使用 @Bean 注解
     * 当然也可以新建一个配置类(@Configuration) 专门管理需要创建的实例
     * @Bean 作用就是使用 DI 往 spring 容器中传教实例，以后可以直接使用 @Resource 取值使用。
     * <p>
     * LoadBalanced：英文就是负载均衡的意思，@LoadBalanced 注解表示 RestTemplate 具有负载均衡的能力。
     * 意味着 RestTemplate 每次发送 http 请求时，都会根据 ribbon 的负载均衡机制向微服务下的某台节点服务器发送请求
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        //RestTemplate 有3个构造器，这里使用无参构造器，底层使用 jdk 原生的 java.net 下 api 进行 http 操作
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(EurekaclientCatApplication.class, args);
    }

}

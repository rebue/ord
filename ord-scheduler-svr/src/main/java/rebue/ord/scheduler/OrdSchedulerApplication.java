package rebue.ord.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringCloudApplication
@EnableScheduling
@EnableFeignClients(basePackages = { "rebue.ord.svr.feign" })
public class OrdSchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrdSchedulerApplication.class, args);
    }
}

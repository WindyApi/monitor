package top.whiteleaf03.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author WhiteLeaf03
 */
@EnableScheduling
@SpringBootApplication
public class WindyApiMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(WindyApiMonitorApplication.class, args);
    }
}

package com.paca.paca;

import com.paca.paca.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class PaCaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaCaApplication.class, args);
    }

}

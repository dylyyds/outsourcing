package com.weichuang.outsourcing.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.weichuang.outsourcing")
public class OutsourcingPortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(OutsourcingPortalApplication.class,args);
    }
}

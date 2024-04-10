package com.zjh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.zjh")
@EnableScheduling
public class Html2PdfApplication {

    public static void main(String[] args) {
        SpringApplication.run(Html2PdfApplication.class, args);
    }

}

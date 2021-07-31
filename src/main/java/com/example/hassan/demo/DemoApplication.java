
// IntelliJ API Decompiler stub source generated from a class file
// Implementation of methods is not available

package com.example.hassan.demo;

import com.example.hassan.demo.transport.M3UALink;
import com.example.hassan.demo.transport.M3UALinkConfiguration;
import com.example.hassan.demo.transport.M3UALinkManager;
import com.example.hassan.demo.transport.M3UALinkManagerImp;
import com.example.hassan.demo.transport.USSDMessage;
import com.example.hassan.model.link.LinkConnectionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {
    private static final Logger logger = LoggerFactory.getLogger(com.example.hassan.demo.DemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(com.example.hassan.demo.DemoApplication.class, args);
    }

    @Bean
    public M3UALinkManager m3UALinkManager() {
        return (M3UALinkManager)new M3UALinkManagerImp();
    }

    @Bean
    public CommandLineRunner runner(M3UALinkManager m3UALinkManager, M3UALinkConfiguration configuration) {
        return args -> {
            M3UALink link = m3UALinkManager.getLink(configuration);
            link.setMessageReceptionInterceptor(());
            link.setConnectionListener(());
            link.connect();
        };
    }
}
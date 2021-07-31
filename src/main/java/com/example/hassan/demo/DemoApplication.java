
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
    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    public DemoApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public M3UALinkManager m3UALinkManager() {
        return new M3UALinkManagerImp();
    }

    @Bean
    public CommandLineRunner runner(M3UALinkManager m3UALinkManager, M3UALinkConfiguration configuration) {
        return (args) -> {
            M3UALink link = m3UALinkManager.getLink(configuration);
            link.setMessageReceptionInterceptor((m3UALink, message) -> {
                logger.info("message received: {}", message.getMessage());
                if ("1".equals(message.getMessage())) {
                    m3UALink.reply(message.createReplay("2", false));
                } else if ("3".equals(message.getMessage())) {
                    m3UALink.reply(message.createReplay("4", true));
                } else {
                    m3UALink.reply(message.createReplay("1-سلام2-نعم", false));
                }

            });
            link.setConnectionListener((m3UALink, info, status) -> {
                logger.info(status.name() + " - " + info);
            });
            link.connect();
        };
    }
}

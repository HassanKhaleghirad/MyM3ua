
// IntelliJ API Decompiler stub source generated from a class file
// Implementation of methods is not available

package com.example.hassan.demo;

@org.springframework.boot.autoconfigure.SpringBootApplication
public class DemoApplication {
    private static final org.slf4j.Logger logger;

    public DemoApplication() { /* compiled code */ }

    public static void main(java.lang.String[] args) { /* compiled code */ }

    @org.springframework.context.annotation.Bean
    public com.example.hassan.demo.transport.M3UALinkManager m3UALinkManager() { /* compiled code */ }

    @org.springframework.context.annotation.Bean
    public org.springframework.boot.CommandLineRunner runner(com.example.hassan.demo.transport.M3UALinkManager m3UALinkManager, com.example.hassan.demo.transport.M3UALinkConfiguration configuration) { /* compiled code */ }
}
package com.xk.demo.dao.mapper;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyCommandLineRunner implements CommandLineRunner {

    @Override
    public void run (String... args) throws Exception {
        System.out.println("MyCommandLineRunner.run()!!!");
    }

}

package com.xk;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

/**
 * https://www.cnblogs.com/xifengxiaoma/p/11116330.html
 * https://github.com/shuzheng/zheng
 * http://localhost:8000//swagger-ui
 */
@SpringBootApplication
@EnableAspectJAutoProxy // 相當xml配置
//@ComponentScan({"com.xk.**.dao.mapper"})
@MapperScan(basePackages = { "com.xk.**.dao.mapper" }) // 掃描該包下相應的 class ，主要是 MyBatis 持久化類
public class BaseApplication extends SpringBootServletInitializer implements TransactionManagementConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);
        System.out.println("項目啟動成功。。。。。。");
    }

    // 啟動類繼承了SpringBootServletInitializer就可以正常部署在外部tomcat中了，主要起到wrb.xml作用
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 這個參數是啟動類，和main方法的第一個參數一樣
        return builder.sources(BaseApplication.class);
    }
    // mvn clean package -Dmaven.test.skip=true

    @Override
    public TransactionManager annotationDrivenTransactionManager() {
        return null;
    }
}



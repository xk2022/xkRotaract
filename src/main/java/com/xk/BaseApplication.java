package com.xk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
public class BaseApplication implements TransactionManagementConfigurer  {

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);
        System.out.println("項目啟動成功。。。。。。");
    }

    @Override
    public TransactionManager annotationDrivenTransactionManager() {
        return null;
    }
}

package com.xk.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * Swagger配置類
 * <p>
 * 訪問：http://localhost:8000/swagger-ui/
 * <p>
 * <!-- 引入Swagger3依赖 -->
 * <!-- https://mvnrepository.com/artifact/io.springfox/springfox-boot-starter -->
 * <dependency>
 * <groupId>io.springfox</groupId>
 * <artifactId>springfox-boot-starter</artifactId>
 * <version>3.0.0</version>
 * </dependency>
 * <p>
 * 添加 Swagger3 SwaggerConfig
 * <p>
 * 如启用了访问权限，还需将swagger相关uri允许匿名访问
 * https://segmentfault.com/a/1190000037455077
 * 修正為高級配置類別
 * https://blog.csdn.net/wangzhihao1994/article/details/108408420
 * 添加请求参数，我们这里把token作为请求头部参数传入后端
 * https://www.cnblogs.com/xifengxiaoma/p/11022146.html
 * <p>
 * 常用注解说明
 * swagger 通过注解接口生成文档，包括接口名，请求方法，参数，返回信息等。
 *
 * @Api: 修饰整个类，用于controller类上
 * @ApiOperation: 描述一个接口，用户controller方法上
 * @ApiParam: 单个参数描述
 * @ApiModel: 用来对象接收参数, 即返回对象
 * @ApiModelProperty: 对象接收参数时，描述对象的字段
 * @ApiResponse: Http响应其中的描述，在ApiResonse中
 * @ApiResponses: Http响应所有的描述，用在
 * @ApiIgnore: 忽略这个API
 * @ApiError: 发生错误的返回信息
 * @ApiImplicitParam: 一个请求参数
 * @ApiImplicitParam: 多个请求参数
 */
//@Configuration
@EnableOpenApi
@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30) // v2 不同 // OpenAPI Specification 的简称
                .apiInfo(apiInfo())
                .enable(true)
                .select()
                //apis： 添加swagger接口提取范围
//                .apis(RequestHandlerSelectors.basePackage("com.xk")) // 设置扫描路径
//                .apis(RequestHandlerSelectors.any()) // 设置扫描路径
//                .apis(RequestHandlerSelectors.withMethodAnnotation(Operation.class))
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
//                .apis(RequestHandlerSelectors.basePackage("com.xk.swaggerv3.controller")) // 设置扫描路径
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger API Doc") // XX项目接口文档
                .description("This is a restful api document of Swagger.") // XX项目描述
                .contact(new Contact("作者", "作者URL", "作者Email"))
                .version("1.0")
                .build();
    }

}

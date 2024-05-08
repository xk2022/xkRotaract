package com.xk;

import com.xk.common.util.NotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by yuan on 2022/04/21
 */
@Controller
public class HelloController {
	
    @GetMapping("/start")
    public String index() {
        return "index";
    }

    // http://127.0.0.1:8080/xxx
    @GetMapping("/404")
    public String index404() {
        String blog = null;
        if (blog == null) {
            throw new NotFoundException("博客不存在");
        }
        return "index";
    }

    // http://127.0.0.1:8080/500
    @GetMapping("/500")
    public String index500() {
        int i = 9 / 0;
        return "index";
    }

    // http://127.0.0.1:8080
//    @GetMapping("/{id}/{name}")
//    public String indexA(@PathVariable Integer id, @PathVariable String name) {
//        System.out.println("------ indexA ------");
//        return "index";
//    }

    /* 方法注解 */
    @ApiOperation(value = "desc of method", notes = "")
    @GetMapping(value = "/hello")
    @Operation(summary = "商品详情,针对得到单个商品的信息")
    public Object hello( /* 参数注解 */ @ApiParam(value = "desc of param", required = true) @RequestParam String name) {
        return "Hello " + name + "!";
    }

}
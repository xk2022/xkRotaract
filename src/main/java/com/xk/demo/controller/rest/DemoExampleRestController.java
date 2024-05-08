package com.xk.demo.controller.rest;

import com.xk.demo.model.dto.DemoExampleExample;
import com.xk.demo.model.po.DemoExample;
import com.xk.demo.service.DemoExampleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 範例 RestController
 * Created by yuan on 2022/06/10
 */
@Api(tags = "範例接口")
@RestController
@RequestMapping("/api/demo/example")
public class DemoExampleRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoExampleRestController.class);

    @Autowired
    private DemoExampleService demoExampleService;

    @ApiOperation(value = "範例列表")
    @GetMapping("/list")
    public List list() {
        return demoExampleService.list(null);
    }

    @ApiOperation(value = "Execute INSERT method")
    @PostMapping("/insert")
    public DemoExample insert(DemoExampleExample resource) {
        return demoExampleService.create(resource);
    }

    @ApiOperation(value = "Execute UPDATE method")
    @PutMapping("/update")
    public DemoExample update(DemoExampleExample resource) {
        return demoExampleService.update(resource.getId(), resource);
    }

    @ApiOperation(value = "Execute DELETE method")
    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        demoExampleService.deleteById(id);
    }

//    @Operation(summary = "商品详情,针对得到单个商品的信息")
//    @GetMapping(value = "/findByUserId")
//    public Object findByUserId(@RequestParam Long userId) {
//        return demoService.findByUserId(userId);
//    }
//
//    @GetMapping(value = "/findAll")
//    public Object findAll() {
//        return demoService.findAll();
//    }

    @GetMapping("/{id}")
    public DemoExample findById(@PathVariable("id") Long id) {
        return demoExampleService.queryById(id);
    }

}
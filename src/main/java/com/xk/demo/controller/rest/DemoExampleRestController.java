package com.xk.demo.controller.rest;

import com.xk.demo.model.bo.DemoExampleSaveReq;
import com.xk.demo.model.vo.DemoExampleSaveResp;
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
 * @author yuan
 * Created by yuan on 2022/06/10
 * Update by yuan at 2024/09/18
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
        return demoExampleService.list();
    }

    @ApiOperation(value = "Execute INSERT method")
    @PostMapping("/insert")
    public DemoExampleSaveResp insert(DemoExampleSaveReq resource) {
        return demoExampleService.create(resource);
    }

    @ApiOperation(value = "Execute UPDATE method")
    @PutMapping("/update")
    public DemoExampleSaveResp update(DemoExampleSaveReq resource) {
        return demoExampleService.update(resource.getId(), resource);
    }

    @ApiOperation(value = "Execute DELETE method")
    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String ids) {
        demoExampleService.deleteByPrimaryKeys(ids);
    }

}
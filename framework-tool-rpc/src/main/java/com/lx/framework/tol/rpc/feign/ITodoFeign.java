package com.lx.framework.tol.rpc.feign;


import com.lx.framework.tool.utils.base.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xin.liu
 * @description 示例Feign
 * @date 2023-04-29  16:53
 * @Version 1.0
 */
@FeignClient(contextId = "ITodoFeign", value = "protocol-service", path = "/todo")
public interface ITodoFeign {

    @PostMapping("/test")
    public Result<String> test(@RequestParam("name")String name);
}

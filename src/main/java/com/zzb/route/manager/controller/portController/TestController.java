package com.zzb.route.manager.controller.portController;

import com.zzb.route.manager.vo.ResultVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试redis哨兵的controller")
@RestController
public class TestController extends ResultVo{
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "kvPut",method = RequestMethod.POST)
    public ResultVo kvPut(String key,String value){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key,value);
        return success();
    }

    @RequestMapping(value = "kvGet",method = RequestMethod.POST)
    public ResultVo kvGet(String key){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String value=ops.get(key);
        return success(value);
    }
}

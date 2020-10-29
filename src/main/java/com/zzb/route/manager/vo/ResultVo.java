package com.zzb.route.manager.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @description: 返回结果vo
 * @author: zzb
 * @time: 2020/9/25 21:56
 */
@Data
@Accessors(chain = true)
public class ResultVo<T> implements Serializable {
    public T data;
    public String msg="";
    public Integer code;
    private long count;

    public static ResultVo success() {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(200);
        resultVo.setMsg("成功");
        return resultVo;
    }

    public static ResultVo success(Object data) {
        ResultVo resultVo = success();
        resultVo.setData(data);
        return resultVo;
    }

    public static ResultVo faild(String msg) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(400);
        resultVo.setMsg(msg);
        return resultVo;
    }
}

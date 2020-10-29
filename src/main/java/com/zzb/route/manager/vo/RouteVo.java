package com.zzb.route.manager.vo;

import com.zzb.route.manager.dataObject.RouteField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@ApiModel
@Accessors(chain = true)
public class RouteVo {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "路由名")
    private String routeName;
    @ApiModelProperty(value = "路由地址")
    private String routeUrl;
    @ApiModelProperty(value = "绑定服务器")
    private String serverName;
    @ApiModelProperty(value = "状态: 服务异常,服务正常")
    private String routeStatus;
    @ApiModelProperty(value = "连接时间")
    private String routeDelay;
    private List<RouteField> fieldsVos;
}

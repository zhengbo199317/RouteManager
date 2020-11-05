package com.zzb.route.manager.dataObject;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class RouteDetail implements Serializable {
    private Integer id;
    private String routeName;
    private String routeUrl;
    private String serverName;
    private String routeStatus;
    private String routeDelay;
    private String workerName;
    private String workerPhone;
    private String serverUrl;
}

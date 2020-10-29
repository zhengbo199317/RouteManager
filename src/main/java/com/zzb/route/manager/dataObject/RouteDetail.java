package com.zzb.route.manager.dataObject;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
@Accessors(chain = true)
public class RouteDetail implements Serializable {
    @Id
    @GeneratedValue
    private String id;
    private String routeName;
    private String routeUrl;
    private String serverName;
    private String routeStatus;
    private String routeDelay;
}

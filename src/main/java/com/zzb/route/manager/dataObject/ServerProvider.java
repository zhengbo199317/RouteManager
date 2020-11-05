package com.zzb.route.manager.dataObject;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ServerProvider {
    private Integer id;
    private String serverName;
    private String url;
}

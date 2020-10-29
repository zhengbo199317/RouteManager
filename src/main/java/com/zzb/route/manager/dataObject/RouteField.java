package com.zzb.route.manager.dataObject;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@Entity
public class RouteField implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;
    private String routeName;
    private String fieldName;
    private String fieldDesc;
    private String fieldValue;
}


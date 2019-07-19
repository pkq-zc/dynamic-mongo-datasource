package com.buydeem.dynamicmongodatasource.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Set;

/**
 * Created by zengchao on 2019/7/19.
 */
@ConfigurationProperties(prefix = "buydeem.data.mongodb.dynamic")
@Data
public class DynamicMongoProperties {
    /**
     * master node name
     */
    private String master = "master";
    /**
     * nodes  config
     */
    private Map<String,MongoProperties> configs;
    /**
     * public converter className
     */
    private Set<Class> commonConverters;
}

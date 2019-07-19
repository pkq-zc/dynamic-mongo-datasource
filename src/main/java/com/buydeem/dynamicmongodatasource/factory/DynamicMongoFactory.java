package com.buydeem.dynamicmongodatasource.factory;

import com.buydeem.dynamicmongodatasource.config.DynamicMongoProperties;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zengchao on 2019/7/19.
 */
@Data
public class DynamicMongoFactory {
    private static final Logger log = LoggerFactory.getLogger(DynamicMongoFactory.class);
    private String masterFactoryName;
    private Map<String,MongoDbFactory> factoryMap = new HashMap<>();

    public DynamicMongoFactory(DynamicMongoProperties properties){
        masterFactoryName = properties.getMaster();
        log.info("设置DynamicMongoFactory主节点为:{}",masterFactoryName);
        Map<String, MongoProperties> configs = properties.getConfigs();
        for (Map.Entry<String, MongoProperties> config : configs.entrySet()) {
            MongoClientOptions.Builder builder = MongoClientOptions.builder()
                    .connectTimeout(3000);
            MongoClientURI uri = new MongoClientURI(config.getValue().getUri(), builder);
            MongoDbFactory factory = new SimpleMongoDbFactory(uri);
            factoryMap.put(config.getKey(),factory);
            log.info("创建DynamicMongoFactory节点{}成功!",config.getKey());
        }
        log.info("创建DynamicMongoFactory个数:{}",factoryMap.size());
    }

    /**
     * 根据名字获取对应的factory
     * @param factoryName
     * @return
     */
    public MongoDbFactory getFactory(String factoryName){
        return factoryMap.getOrDefault(factoryName,factoryMap.get(masterFactoryName));
    }
}

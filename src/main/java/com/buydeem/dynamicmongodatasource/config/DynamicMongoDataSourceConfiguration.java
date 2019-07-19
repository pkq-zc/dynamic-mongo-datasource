package com.buydeem.dynamicmongodatasource.config;

import com.buydeem.dynamicmongodatasource.factory.DynamicMongoFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 多数据源动态配置类
 * Created by zengchao on 2019/7/19.
 */
@Configuration
@EnableConfigurationProperties(DynamicMongoProperties.class)
@AutoConfigureBefore(value = {MongoAutoConfiguration.class,MongoDataAutoConfiguration.class})
public class DynamicMongoDataSourceConfiguration {
    private final DynamicMongoProperties properties;
    private final DynamicMongoFactory factory;
    private final Map<String,MongoTemplate> templateMap = new HashMap<>();

    public DynamicMongoDataSourceConfiguration(DynamicMongoProperties properties) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        this.properties = properties;
        this.factory = new DynamicMongoFactory(properties);
        //创建转换器
        Map<String, Converter> converterMap = loadConvert(properties.getCommonConverters());
        //创建Template
        Map<String, MongoDbFactory> factoryMap = factory.getFactoryMap();
        for (Map.Entry<String, MongoDbFactory> factoryEntry : factoryMap.entrySet()) {
            MongoTemplate template = new MongoTemplate(factoryEntry.getValue());
            Set<Converter> tempConverts = converterMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
            CustomConversions customConversions = new CustomConversions(CustomConversions.StoreConversions.NONE,tempConverts);
            MappingMongoConverter converter = (MappingMongoConverter) template.getConverter();
            converter.setCustomConversions(customConversions);
            converter.afterPropertiesSet();
            templateMap.put(factoryEntry.getKey(),template);
        }
    }

    @Bean
    public MongoTemplate createMasterTemplate(){
        return templateMap.get(properties.getMaster());
    }

    private Map<String,Converter> loadConvert(Collection<Class> classList) throws IllegalAccessException, InstantiationException {
        Map<String,Converter> converterMap = new HashMap<>();
        for (Class clazz : classList) {
            String name = clazz.getName();
            Converter converter = (Converter) clazz.newInstance();
            converterMap.put(name,converter);
        }
        return converterMap;
    }
}

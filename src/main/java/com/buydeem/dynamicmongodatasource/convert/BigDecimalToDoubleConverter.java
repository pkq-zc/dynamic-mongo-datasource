package com.buydeem.dynamicmongodatasource.convert;

import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

/**
 * BigDecimal 转 Double 转换器
 * Created by zengchao on 2018/6/8.
 */
public class BigDecimalToDoubleConverter implements Converter<BigDecimal, Double> {
    @Override
    public Double convert(BigDecimal value) {
        return value.doubleValue();
    }
}

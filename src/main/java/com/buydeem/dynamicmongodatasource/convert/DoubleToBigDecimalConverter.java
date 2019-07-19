package com.buydeem.dynamicmongodatasource.convert;

import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

/**
 * Double 转BigDecimal
 * @author zengchao
 * Created by zengchao on 2018/6/8.
 */
public class DoubleToBigDecimalConverter implements Converter<Double, BigDecimal> {
    @Override
    public BigDecimal convert(Double value) {
        return new BigDecimal(value.toString());
    }
}

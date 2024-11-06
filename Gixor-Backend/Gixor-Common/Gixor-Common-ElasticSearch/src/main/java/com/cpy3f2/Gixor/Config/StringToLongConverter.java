package com.cpy3f2.Gixor.Config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class StringToLongConverter implements Converter<String, Long> {
    @Override
    public Long convert(String source) {
        if (source == null || source.isEmpty()) {
            return 0L;
        }
        
        try {
            // 移除所有空格
            source = source.trim().toLowerCase();
            
            // 处理带单位的数字
            double v = Double.parseDouble(source.substring(0, source.length() - 1));
            if (source.endsWith("k")) {
                return (long)(v * 1000);
            } else if (source.endsWith("m")) {
                double value = v;
                return (long)(value * 1000000);
            } else if (source.endsWith("b")) {
                double value = v;
                return (long)(value * 1000000000);
            }
            
            // 处理普通数字
            return Long.parseLong(source);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}
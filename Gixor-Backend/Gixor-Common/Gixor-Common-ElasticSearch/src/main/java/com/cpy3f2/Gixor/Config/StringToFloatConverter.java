package com.cpy3f2.Gixor.Config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class StringToFloatConverter implements Converter<String, Float> {
    @Override
    public Float convert(String source) {
        if (source == null || source.isEmpty()) {
            return 0f;
        }
        // 移除百分号和空格
        source = source.replace("%", "").trim();
        try {
            return Float.parseFloat(source);
        } catch (NumberFormatException e) {
            return 0f;
        }
    }
} 
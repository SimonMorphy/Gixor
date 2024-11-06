package com.cpy3f2.Gixor.Convertor;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-06 03:47
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-06 03:47
 */
public class HtmlConvertor {
    public static String convert(String owner, String repo, String html) {
        // 替换相对路径的图片
        String rawBaseUrl = "https://raw.githubusercontent.com/" + owner + "/" + repo + "/master/";

        // 替换 src="./pic/xxx" 格式的图片路径
        html = html.replaceAll("src=\"\\./pic/", "src=\"" + rawBaseUrl + "pic/");

        // 替换 src="pic/xxx" 格式的图片路径
        html = html.replaceAll("src=\"pic/", "src=\"" + rawBaseUrl + "pic/");

        // 添加基础样式
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<style>" +
                "body { padding: 15px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                html +
                "</body>" +
                "</html>";
    }
}

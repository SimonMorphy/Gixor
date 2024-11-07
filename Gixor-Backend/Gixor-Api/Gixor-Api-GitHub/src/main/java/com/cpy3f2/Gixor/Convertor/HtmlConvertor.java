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
        String rawBaseUrl = "https://raw.githubusercontent.com/" + owner + "/" + repo + "/master/";

        return html.replaceAll("src=\"\\./pic/", "src=\"" + rawBaseUrl + "pic/")
                .replaceAll("src=\"pic/", "src=\"" + rawBaseUrl + "pic/");
    }
}

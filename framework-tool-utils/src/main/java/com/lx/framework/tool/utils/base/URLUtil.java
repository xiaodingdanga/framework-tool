package com.lx.framework.tool.utils.base;

import cn.hutool.core.text.AntPathMatcher;

/**
 * @author xin.liu
 * @description URL 正则匹配工具
 * @date 2023-02-07  16:58
 * @Version 1.0
 */
public class URLUtil {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();


    /**
     * @description 是否为白名单
     * @param currentURL
     * @param white
     * @return: boolean
     * @author xin.liu
     * @date 2023/2/7 16:58
     */
    public static boolean isWhiteURL(String currentURL,String[] white) {
        for (String whiteURL : white) {
            if (pathMatcher.match(whiteURL, currentURL)) {
                return true;
            }
        }
        return false;
    }

}

package com.libs.core.common.utils;

import java.util.List;

/**
 * <pre>
 *     author : amos
 *     e-mail : hui.li1@yintech.cn
 *     time   : 2019/08/06 19:02
 *     desc   : list 列表是否为空检测
 *     version: 1.0
 * </pre>
 */
public class ListUtils {

    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }
}

package com.lkx.jvm.sandbox.core.util;

import com.lkx.jvm.sandbox.core.exception.RequireParamException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 17:43
 */
public class CheckUtils {

    /**
     * 值不能为空
     *
     * @param obj
     * @param fieldName
     */
    public static void isRequireNotNull(Object obj, String fieldName) {
        if (obj == null || StringUtils.isEmpty(obj.toString())) {
            throw new RequireParamException("required [" + fieldName + "] is not null!");
        }
    }

}

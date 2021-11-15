package com.alibaba.jvm.sandbox.module.manager.util;

import com.google.common.base.Splitter;

import java.util.List;

/**
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/12 - 16:43
 */
public class IPUtils {

    public static String genIpHex(String ip) {
        List<String> items = Splitter.on(".").splitToList(ip);
        byte[] bytes = new byte[4];

        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) Integer.parseInt(items.get(i));
        }

        StringBuilder sb = new StringBuilder(bytes.length / 2);

        for (byte b : bytes) {
            sb.append(Integer.toHexString((b >> 4) & 0x0F));
            sb.append(Integer.toHexString(b & 0x0F));
        }
        return sb.toString();
    }

}

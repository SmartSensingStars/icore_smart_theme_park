package com.larcloud.util;

import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-4-3
 * Time: 下午8:07
 * To change this template use File | Settings | File Templates.
 */
public class ByteUtil {
    public static byte[] getBytesFromInteger(Integer value){
        return  ByteBuffer.allocate(4).putInt(value).array();
    }
    public static Integer getIntegerFromBytes(byte[] value){
        ByteBuffer wrapped = ByteBuffer.wrap(value); // big-endian by default
        Integer num = wrapped.getInt(); // 1
        return num;
    }
}

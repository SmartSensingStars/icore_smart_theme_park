package com.larcloud.util;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-1-13
 * Time: 下午6:56
 * To change this template use File | Settings | File Templates.
 */
public class TlvCodec {
    private static Logger log = Logger.getLogger(TlvCodec.class.getName());

    public static int bytes2int(byte[] res) {
    // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

        int targets = (res[1] & 0xff) | ((res[0] << 8) & 0xff00); // | 表示安位或
               // | ((res[2] << 24) >>> 8) | (res[3] << 24);
        return targets;
    }

    /**
     * Reads TLV values for a given hex string.
     */
    public static byte[][] readTLV(String tlvHexString, int tag) {
        return readTLV(hexStringToByteArray(tlvHexString), tag);
    }

    /**
     * Reads TLV values for a given byte array.
     */
    public static byte[][] readTLV(byte[] tlv, int tag) {
        if (tlv == null || tlv.length < 1) {
            throw new IllegalArgumentException("Invalid TLV");
        }

        int c = 0;
        ArrayList al = new ArrayList();

        ByteArrayInputStream is = null;
        try {
            is = new ByteArrayInputStream(tlv);

            while ((c = is.read()) != -1) {
                if (c == tag){
                    log.debug("Got tag");
                    if ((c = is.read()) != -1){
                        byte[] value = new byte[c];
                        is.read(value,0,c);
                        al.add(value);
                    }
                }
            }
        } finally {
            if (is != null) {
                try{
                    is.close();
                }catch (IOException e){
                    log.error(e);
                }
            }
        }
        log.debug("Got " + al.size() + " values for tag "
                + Integer.toHexString(tag));
        byte[][] vals = new byte[al.size()][];
        al.toArray(vals);
        return vals;
    }

    /**
     * Converts a hex string to byte array.
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}

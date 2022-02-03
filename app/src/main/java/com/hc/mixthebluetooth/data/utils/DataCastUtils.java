package com.hc.mixthebluetooth.data.utils;

public class DataCastUtils {

  public static int byteArray2int(byte[] data, int offset) {
    for (int i = 0; i < 4; i++) {
      data[offset + i] = (byte) (data[offset + i] - 128);
    }
    return ((((int) data[offset + 3]) + 128) << 24)
        | ((((int) data[offset + 2]) + 128) << 16)
        | ((((int) data[offset + 1]) + 128) << 8)
        | (((int) data[offset]) + 128);
  }

  public static float byte2float(byte[] data, int offset) {
    return Float.intBitsToFloat(byteArray2int(data, offset));
  }
}

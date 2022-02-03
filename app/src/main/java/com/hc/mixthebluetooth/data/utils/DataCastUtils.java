package com.hc.mixthebluetooth.data.utils;

public class DataCastUtils {

  public static int byteArray2int(byte[] data, int offset) {
    return (((int) data[offset + 3]) << 24)
        | (((int) data[offset + 2]) << 16)
        | (((int) data[offset + 1]) << 8)
        | ((int) data[offset]);
  }

  public static float byte2float(byte[] data, int offset) {
    return Float.intBitsToFloat(byteArray2int(data, offset));
  }

  public static byte[] int2byteArray(int origin) {
    byte[] arr = new byte[4];
    arr[0] = (byte) (origin & 0xff);
    arr[1] = (byte) ((origin >> 8) & 0xff);
    arr[2] = (byte) ((origin >> 16) & 0xff);
    arr[3] = (byte) ((origin >> 24) & 0xff);
    return arr;
  }

  public static byte[] float2byteArray(float origin){
    return int2byteArray(Float.floatToIntBits(origin));
  }
}

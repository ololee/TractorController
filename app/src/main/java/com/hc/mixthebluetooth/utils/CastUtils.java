package com.hc.mixthebluetooth.utils;

public class CastUtils {
  public static int[] byteArray2IntArray(byte[] ba,int len){
    int[] result = new int[len];
    System.arraycopy(ba,0,result,0,len);
    /*
    for (int i = 0; i < len; i++) {
      result[i]=ba[i];
    }
    * */
    return result;
  }

  public static byte[] intArray2ByteArray(int[] ia,int len){
    byte[] result = new byte[len];
    //System.arraycopy(ia,0,result,0,len);
    for (int i = 0; i < len; i++) {
      result[i]= (byte) ia[i];
    }
    return result;
  }
}

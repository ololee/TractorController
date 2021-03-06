package com.hc.mixthebluetooth.data;

import android.util.Log;
import com.hc.mixthebluetooth.data.exception.DataErrorException;
import com.hc.mixthebluetooth.data.utils.DataCastUtils;
import com.hc.mixthebluetooth.utils.CRCCheckUtils;
import com.hc.mixthebluetooth.utils.CastUtils;

public class DataDealUtils {
  public static final String TAG =DataDealUtils.class.getSimpleName();
  private static DataModel dataModel = new DataModel();



  public static DataModel formatData(byte[] data) {
    if (data == null) {
      return null;
    }
    if (data[0] != 0x5a) {
      throw new DataErrorException();
    }
    switch (data[1]) {
      case (byte) 0xc1:
        Log.e(TAG, "formatData:c1,isC1DataValid: "+ isC1DataValid(data));
        c1Data(data);
        break;
      case (byte) 0xb1:
        Log.e(TAG, "formatData:b1,isB1DataValid: "+ isB1DataValid(data));
        b1Data(data);
        break;
      case (byte) 0xf1:
        Log.e(TAG, "formatData:F1,isF1DataValid: "+ isF1DataValid(data));
        f1Data(data);
        break;
    }

    return dataModel;
  }

  private static void c1Data(byte[] data) {
    try {
      /**
       * 横向偏差
       */
      dataModel.setLateralDeviation(DataCastUtils.byte2float(data, 2));
      /**
       * 航向偏差
       */
      dataModel.setCourseDeviation(DataCastUtils.byte2float(data, 6));
      /**
       * 前轮转角
       */
      dataModel.setFrontWheelAngle(DataCastUtils.byte2float(data, 10));
      /**
       * rtk航向
       */
      dataModel.setRtkDirection(DataCastUtils.byte2float(data, 14));
      /**
       * 车辆X位置
       */
      dataModel.setVehicleX(DataCastUtils.byte2float(data, 18));
      /**
       * 车辆Y位置
       */
      dataModel.setVehicleY(DataCastUtils.byte2float(data, 22));
      /**
       * rtk模式
       */
      dataModel.setRtkMode(data[26]);
    } catch (Exception e) {
      throw new DataErrorException();
    }
  }

  private static void b1Data(byte[] data) {
    try {
      dataModel.setRtkMode(data[2]);
    } catch (Exception e) {
      throw new DataErrorException();
    }
  }

  private static void f1Data(byte[] data) {
    try {
      dataModel.setBaseLineAngle(DataCastUtils.byte2float(data, 2));
    } catch (Exception e) {
      throw new DataErrorException();
    }
  }

  public static byte[] sendControlCodeFunc(int functionCode) {
    byte[] data = new byte[4];
    data[0] = (byte) 0xa5;
    data[1] = (byte) 0xff;
    data[2] = (byte) functionCode;
    data[3] = calcCRC8CheckData(data,3);
    return data;
  }

  @Deprecated
  public static byte[] sendDirectionCodeFunc(float direction) {
    byte[] dirbytes = DataCastUtils.float2ByteArray(direction);
    byte[] data = new byte[7];
    data[0] = (byte) 0xa5;
    data[1] = (byte) 0xf1;
    data[2] = dirbytes[0];
    data[3] = dirbytes[1];
    data[4] = dirbytes[2];
    data[5] = dirbytes[3];
    data[6] = calcCRC8CheckData(data,6);
    return data;
  }

  public static byte[] sendDirectionCodeFunc(int direction) {
    byte[] data = new byte[4];
    data[0] = (byte) 0xa5;
    data[1] = (byte) 0xf1;
    data[2] = (byte) direction;
    data[3] = calcCRC8CheckData(data,3);
    return data;
  }

  public static byte[] sendThrottleCodeFunc(int direction) {
    byte[] data = new byte[4];
    data[0] = (byte) 0xa5;
    data[1] = (byte) 0xf2;
    data[2] = (byte) direction;
    data[3] = calcCRC8CheckData(data,3);
    return data;
  }

  public static byte[] sendLiftThrottleCodeFunc(int direction) {
    byte[] data = new byte[4];
    data[0] = (byte) 0xa5;
    data[1] = (byte) 0xf3;
    data[2] = (byte) direction;
    data[3] = calcCRC8CheckData(data,3);
    return data;
  }

  private static boolean validData(byte[] data,int len){
    return CRCCheckUtils.CRC8_Table_Check(CastUtils.byteArray2IntArray(data,len),len,data[len])!=0;
  }

  /**
   * c1数据验证
   * @param data
   * @return
   */
  public static boolean isC1DataValid(byte[] data){
    return CRCCheckUtils.CRC8_Table_Check(CastUtils.byteArray2IntArray(data,26),26,data[26])!=0;
  }

  /**
   * B1数据验证
   * @param data
   * @return
   */
  public static boolean isB1DataValid(byte[] data){
    return CRCCheckUtils.CRC8_Table_Check(CastUtils.byteArray2IntArray(data,6),6,data[6])!=0;
  }

  /**
   * F1数据验证
   * @param data
   * @return
   */
  public static boolean isF1DataValid(byte[] data){
    return CRCCheckUtils.CRC8_Table_Check(CastUtils.byteArray2IntArray(data,3),3,data[3])!=0;
  }

  public static byte calcCRC8CheckData(byte[] data,int len){
    return (byte) CRCCheckUtils.CRC8_Table(CastUtils.byteArray2IntArray(data,len),len);
  }

  public static byte calcCRC8CheckData(byte[] data){
    return (byte) CRCCheckUtils.CRC8_Table(CastUtils.byteArray2IntArray(data,data.length),data.length);
  }
}

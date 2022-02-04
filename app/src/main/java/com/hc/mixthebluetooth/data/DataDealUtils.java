package com.hc.mixthebluetooth.data;

import com.hc.mixthebluetooth.data.exception.DataErrorException;
import com.hc.mixthebluetooth.data.utils.DataCastUtils;

public class DataDealUtils {

  public static DataModel formatData(byte[] data) {
    if (data == null) {
      return null;
    }
    if (data.length < 27) {
      throw new DataErrorException();
    }
    /*if (data[0] != (byte) 0x5a || data[1] != (byte) 0xc1 || data[27] != 0x1c) {
      throw new DataErrorException();
    }*/
    DataModel dataModel = new DataModel();

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
    } catch (Exception e) {
      throw new DataErrorException();
    }
    return dataModel;
  }

  public static byte[] sendABCDPointsFunc(int functionCode) {
    byte[] data = new byte[4];
    data[0] = (byte) 0xa5;
    data[1] = (byte) 0xff;
    data[2] = (byte) functionCode;
    data[3] = 0x00;
    return data;
  }
}

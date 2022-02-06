package com.hc.mixthebluetooth.data;

public class DataModel {
  /*
   * 横向偏差
   * */
  private float lateralDeviation;
  /*
   * 航向偏差
   * */
  private float courseDeviation;
  /**
   * 前轮转角
   */
  private float frontWheelAngle;

  /**
   * rtk航向
   */
  private float rtkDirection;

  /**
   * 车辆X的位置
   */
  private float vehicleX;

  /**
   * 车辆Y的位置
   */
  private float vehicleY;

  /**
   * rtk模式
   */
  private int rtkMode;

  private float baseLineAngle;


  public DataModel() {
  }

  public DataModel(float lateralDeviation, float courseDeviation, float frontWheelAngle,
      float rtkDirection, float vehicleX, float vehicleY) {
    this.lateralDeviation = lateralDeviation;
    this.courseDeviation = courseDeviation;
    this.frontWheelAngle = frontWheelAngle;
    this.rtkDirection = rtkDirection;
    this.vehicleX = vehicleX;
    this.vehicleY = vehicleY;
  }

  public DataModel(float lateralDeviation, float courseDeviation, float frontWheelAngle,
      float rtkDirection, float vehicleX, float vehicleY, int rtkMode, float baseLineAngle) {
    this.lateralDeviation = lateralDeviation;
    this.courseDeviation = courseDeviation;
    this.frontWheelAngle = frontWheelAngle;
    this.rtkDirection = rtkDirection;
    this.vehicleX = vehicleX;
    this.vehicleY = vehicleY;
    this.rtkMode = rtkMode;
    this.baseLineAngle = baseLineAngle;
  }

  public float getLateralDeviation() {
    return lateralDeviation;
  }

  public void setLateralDeviation(float lateralDeviation) {
    this.lateralDeviation = lateralDeviation;
  }

  public float getCourseDeviation() {
    return courseDeviation;
  }

  public void setCourseDeviation(float courseDeviation) {
    this.courseDeviation = courseDeviation;
  }

  public float getFrontWheelAngle() {
    return frontWheelAngle;
  }

  public void setFrontWheelAngle(float frontWheelAngle) {
    this.frontWheelAngle = frontWheelAngle;
  }

  public float getRtkDirection() {
    return rtkDirection;
  }

  public void setRtkDirection(float rtkDirection) {
    this.rtkDirection = rtkDirection;
  }

  public float getVehicleX() {
    return vehicleX;
  }

  public void setVehicleX(float vehicleX) {
    this.vehicleX = vehicleX;
  }

  public float getVehicleY() {
    return vehicleY;
  }

  public void setVehicleY(float vehicleY) {
    this.vehicleY = vehicleY;
  }

  public int getRtkMode() {
    return rtkMode;
  }

  public void setRtkMode(int rtkMode) {
    this.rtkMode = rtkMode;
  }

  public float getBaseLineAngle() {
    return baseLineAngle;
  }

  public void setBaseLineAngle(float baseLineAngle) {
    this.baseLineAngle = baseLineAngle;
  }

  @Override public String toString() {
    return "DataModel{" +
        "lateralDeviation=" + lateralDeviation +
        ", courseDeviation=" + courseDeviation +
        ", frontWheelAngle=" + frontWheelAngle +
        ", rtkDirection=" + rtkDirection +
        ", vehicleX=" + vehicleX +
        ", vehicleY=" + vehicleY +
        ", rtkMode=" + rtkMode +
        ", baseLineAngle=" + baseLineAngle +
        '}';
  }
}

package io.training.week5.model;

import java.math.BigInteger;

public class OrderNumber {

  private BigInteger orderNumber;

  public OrderNumber(BigInteger orderNumber) {
    this.orderNumber = orderNumber;
  }

  public OrderNumber() {}

  public Long getOrderNumber() {
    return orderNumber.longValue();
  }

  public void setOrderNumber(BigInteger orderNumber) {
    this.orderNumber = orderNumber;
  }
}

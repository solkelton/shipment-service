package io.training.week5.model;

public class OrderLineDisplay {
  private String productName;
  private int quantity;

  public OrderLineDisplay(String productName, int quantity) {
    this.productName = productName;
    this.quantity = quantity;
  }

  public OrderLineDisplay() {}

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}

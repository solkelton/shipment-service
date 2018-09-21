package io.training.week5.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.training.week5.model.Account;
import io.training.week5.model.Address;
import io.training.week5.model.ShipmentDisplay;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Transient;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@SqlResultSetMapping(
    name="shipmentMapping",
    classes=@ConstructorResult(
        targetClass= ShipmentDisplay.class,
        columns={
            @ColumnResult(name="shippedDate", type=LocalDateTime.class),
            @ColumnResult(name="deliveryDate", type=LocalDateTime.class),
        }))
@NamedNativeQuery(
    name="retrieveShipmentDates",
    query="select delivery_date as deliveryDate, shipped_date as shippedDate "
        + "from shipment "
        + "where id=?1",
    resultSetMapping = "shipmentMapping"
)
public class Shipment {
  @Id
  @GeneratedValue
  @JsonIgnore
  private long id;
//  @JoinColumn(name="accountId")
//  @JsonIgnore
  private long accountId;
//  @JoinColumn(name="addressId")
//  @JsonIgnore
  private long addressId;
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime shippedDate;
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime deliveryDate;
  @Transient
  private Account account;
  @Transient
  private Address address;

  public Shipment(long accountId, long addressId,  LocalDateTime shippedDate, LocalDateTime deliveryDate) {
    this.accountId = accountId;
    this.addressId = addressId;
    this.shippedDate = shippedDate;
    this.deliveryDate = deliveryDate;
    this.account = null;
    this.address = null;
  }

  public Shipment(LocalDateTime shippedDate, LocalDateTime deliveryDate) {
    this.shippedDate = shippedDate;
    this.deliveryDate = deliveryDate;
  }

  public Shipment() {}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public LocalDateTime getShippedDate() {
    return shippedDate;
  }

  public LocalDateTime getDeliveryDate() {
    return deliveryDate;
  }

  public void setShippedDate(LocalDateTime shippedDate) {
    this.shippedDate = shippedDate;
  }

  public void setDeliveryDate(LocalDateTime deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  @JsonIgnore
  public long getAccountId() {
    return accountId;
  }

  @JsonProperty
  public void setAccountId(long accountId) {
    this.accountId = accountId;
  }

  @JsonIgnore
  public long getAddressId() {
    return addressId;
  }

  @JsonProperty
  public void setAddressId(long addressId) {
    this.addressId = addressId;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "Shipment{" +
        "accountId=" + accountId +
        ", addressId=" + addressId +
        ", shippedDate=" + shippedDate +
        ", deliveryDate=" + deliveryDate +
        '}';
  }
}

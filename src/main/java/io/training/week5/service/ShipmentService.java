package io.training.week5.service;

import io.training.week5.model.Account;
import io.training.week5.model.Address;
import io.training.week5.entity.Shipment;
import io.training.week5.model.OrderLineDisplay;
import io.training.week5.model.OrderNumber;
import io.training.week5.model.ShipmentDisplay;
import io.training.week5.repo.ShipmentRepository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService {
  private ShipmentRepository shipmentRepository;
  @Autowired AccountService accountService;
  @Autowired AddressService addressService;
  @Autowired OrdersService ordersService;
  @Autowired OrderLineItemsService orderLineItemsService;

  public ShipmentService(ShipmentRepository shipmentRepository) {
    this.shipmentRepository = shipmentRepository;
  }

  public Shipment retrieveShipment(long id) {
    Shipment shipment = shipmentRepository.getShipmentById(id);

    if(validateShipment(shipment)) {
      Account account = accountService.retrieveAccount(shipment.getAccountId());
      Address address = addressService.retrieveAddress(shipment.getAccountId(), shipment.getAddressId());
      shipment.setAccount(account);
      shipment.setAddress(address);
    }
    return shipment;
  }

  public ShipmentDisplay retrieveShipmentDates(long id) {
    return shipmentRepository.retrieveShipmentDates(id);
  }

  public List<ShipmentDisplay> retrieveAccountShipments(long accountId) {
    List<Shipment> shipmentList = shipmentRepository.findAllByAccountId(accountId);
    List<OrderNumber> orderNumberList = retrieveOrderNumber(accountId);
    int size = 0;
    if(shipmentList.size() == orderNumberList.size()) { size = shipmentList.size() | orderNumberList.size(); }
    List<ShipmentDisplay> shipmentDisplayList = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      Shipment shipment = shipmentList.get(i);
      ShipmentDisplay shipmentDisplay = new ShipmentDisplay(shipment.getShippedDate(), shipment.getDeliveryDate());
      shipmentDisplay.setOrderNumber(orderNumberList.get(i).getOrderNumber());
      shipmentDisplay.setOrderLineItems(retrieveOrderLineDisplay(shipment.getId()));
      shipmentDisplayList.add(shipmentDisplay);
    }
    return shipmentDisplayList;
  }

  public Shipment updateShipment(long id, Shipment updatedShipment) {
    Shipment originalShipment = retrieveShipment(id);

    if(validateShipment(originalShipment)) {
      Shipment newShipment = update(originalShipment, updatedShipment);
      return shipmentRepository.save(newShipment);
    }
    return new Shipment();
  }

  public Shipment addShipment(Shipment shipment) {
    if(validateShipment(shipment)) {
      return shipmentRepository.save(shipment);
    }
    return new Shipment();
  }

  public boolean removeShipment(long shipmentId) {
    Shipment shipment = shipmentRepository.getShipmentById(shipmentId);
    if(validateShipment(shipment)) {
      shipmentRepository.deleteShipmentById(shipmentId);
      return true;
    }
    return false;
  }

  private Account retrieveAccount(long id) { return accountService.retrieveAccount(id); }
  private Address retrieveAddress(long accountId, long id) { return addressService.retrieveAddress(accountId, id); }
  private List<OrderNumber> retrieveOrderNumber(long accountId) { return ordersService.retrieveOrderNumber(accountId); }
  private List<OrderLineDisplay> retrieveOrderLineDisplay(long ordersId ) { return orderLineItemsService.retrieveShipmentDisplay(ordersId); }

  private boolean validateShipment(Shipment shipment) {
    if(shipment == null) return false;
    if(shipment.getAccountId() == 0) return false;
    if(shipment.getAddressId() == 0) return false;
    if(shipment.getShippedDate() == null) return false;
    if(shipment.getDeliveryDate() == null) return false;
    return true;
  }

  private Shipment update(Shipment original, Shipment updated) {
    Shipment newShipment = new Shipment();
    newShipment.setId(original.getId());

    if(updated == null) return original;

    if(updated.getAccountId() == 0){ newShipment.setAccountId(original.getAccountId()); }
    else newShipment.setAccountId(updated.getAccountId());

    if(updated.getAddressId() == 0) newShipment.setAddressId(original.getAddressId());
    else newShipment.setAddressId(updated.getAddressId());

    if(updated.getShippedDate() == null) newShipment.setShippedDate(original.getShippedDate());
    else newShipment.setShippedDate(updated.getShippedDate());

    if(updated.getDeliveryDate() == null) newShipment.setDeliveryDate(original.getDeliveryDate());
    else newShipment.setDeliveryDate(updated.getDeliveryDate());

    return newShipment;
  }
}

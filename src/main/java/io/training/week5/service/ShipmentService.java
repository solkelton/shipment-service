package io.training.week5.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.training.week5.client.AccountClient;
import io.training.week5.client.AddressClient;
import io.training.week5.client.OrderLineItemsClient;
import io.training.week5.client.OrdersServiceClient;
import io.training.week5.model.Account;
import io.training.week5.model.Address;
import io.training.week5.entity.Shipment;
import io.training.week5.model.OrderLineDisplay;
import io.training.week5.model.OrderNumber;
import io.training.week5.model.ShipmentDisplay;
import io.training.week5.repo.ShipmentRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private ShipmentRepository shipmentRepository;
  @Autowired
  AccountClient accountClient;
  @Autowired
  AddressClient addressClient;
  @Autowired
  OrdersServiceClient ordersServiceClient;
  @Autowired
  OrderLineItemsClient orderLineItemsClient;

  public ShipmentService(ShipmentRepository shipmentRepository) {
    this.shipmentRepository = shipmentRepository;
  }

  @HystrixCommand(fallbackMethod = "retrieveShipmentFallBack")
  public Shipment retrieveShipment(long id) {
    Shipment shipment = shipmentRepository.getShipmentById(id);

    if(validateShipment(shipment)) {
      logger.info("Valid Shipment found at retrieveShipment Function");
      Account account = accountClient.retrieveAccount(shipment.getAccountId());
      Address address = addressClient.retrieveAddress(shipment.getAccountId(), shipment.getAddressId());
      shipment.setAccount(account);
      shipment.setAddress(address);
      return shipment;
    }
    logger.debug("Invalid Shipment Id at retrieveShipment Function");
    logger.debug("Shipment Id: {}", id);
    return new Shipment();
  }

  public Shipment retrieveShipmentFallBack(long id) {
    logger.debug("Account Serivce is Down");
    Shipment shipment = shipmentRepository.getShipmentById(id);
    if(validateShipment(shipment)) {
      logger.info("Valid Shipment found at retrieveShipmentFallBack Function");
      return shipment;
    }
    logger.debug("Invalid Shipment Id at retrieveShipmentFallBack Function");
    logger.debug("Shipment Id: {}", id);
    return new Shipment();
  }

  public ShipmentDisplay retrieveShipmentDates(long id) {
    Shipment shipment = retrieveShipment(id);
    if(validateShipment(shipment)) {
      logger.info("Valid Shipment found at retrieveShipmentDates function");
      return shipmentRepository.retrieveShipmentDates(id);
    }
    logger.debug("Invalid Shipment Id at retrieveShipmentDates Function");
    logger.debug("Shipment Id: {}", id);
    return new ShipmentDisplay();
  }

  @HystrixCommand(fallbackMethod = "retrieveAccountShipmentsFallBack")
  public List<ShipmentDisplay> retrieveAccountShipments(long accountId) {
    List<Shipment> shipmentList = shipmentRepository.getShipmentsByAccountId(accountId);
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

  public List<ShipmentDisplay> retrieveAccountShipmentsFallBack(long accountId) {
    List<Shipment> shipmentList = shipmentRepository.getShipmentsByAccountId(accountId);
    List<ShipmentDisplay> shipmentDisplayList = new ArrayList<ShipmentDisplay>() {{
      for(Shipment shipment : shipmentList) {
        add(new ShipmentDisplay(shipment.getShippedDate(), shipment.getDeliveryDate()));
      }
    }};
    return shipmentDisplayList;
  }

  public Shipment updateShipment(long id, Shipment updatedShipment) {
    Shipment originalShipment = retrieveShipment(id);
    if(validateShipment(originalShipment)) {
      logger.info("Valid Shipment Updated");
      Shipment newShipment = update(originalShipment, updatedShipment);
      return shipmentRepository.save(newShipment);
    }
    logger.debug("Invalid Shipment Attempted to Updated at Id: {}", id);
    logger.debug("Shipment: {}", updatedShipment.toString());
    return new Shipment();
  }

  public Shipment addShipment(Shipment shipment) {
    if(validateShipment(shipment)) {
      logger.info("Valid Shipment Added");
      return shipmentRepository.save(shipment);
    }
    logger.debug("Invalid Shipment Attempted to Add");
    logger.debug("Shipment: {}", shipment.toString());
    return new Shipment();
  }

  public boolean removeShipment(long shipmentId) {
    Shipment shipment = shipmentRepository.getShipmentById(shipmentId);
    if(validateShipment(shipment)) {
      logger.info("Valid Shipment Removed");
      shipmentRepository.deleteShipmentById(shipmentId);
      return true;
    }
    logger.debug("Invalid Shipment Attempted to Remove");
    logger.debug("Shipment Id: {}", shipmentId);
    return false;
  }

  private Account retrieveAccount(long id) { return accountClient.retrieveAccount(id); }
  private Address retrieveAddress(long accountId, long id) { return addressClient.retrieveAddress(accountId, id); }
  private List<OrderNumber> retrieveOrderNumber(long accountId) { return ordersServiceClient.retrieveOrderNumber(accountId); }
  private List<OrderLineDisplay> retrieveOrderLineDisplay(long ordersId ) { return orderLineItemsClient.retrieveShipmentDisplay(ordersId); }

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

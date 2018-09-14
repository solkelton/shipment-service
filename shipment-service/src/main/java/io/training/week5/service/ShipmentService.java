package io.training.week5.service;

import io.training.week5.model.Account;
import io.training.week5.model.Address;
import io.training.week5.entity.Shipment;
import io.training.week5.model.OrderLineDisplay;
import io.training.week5.model.ShipmentDisplay;
import io.training.week5.repo.ShipmentRepository;
import java.util.ArrayList;
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
    Shipment shipment = new Shipment();
    Optional<Shipment> query = shipmentRepository.findById(id);
    if(query.isPresent()) {
      shipment = query.get();
      Account account = accountService.retrieveAccount(shipment.getAccountId());
      Address address = addressService.retrieveAddress(shipment.getAccountId(), shipment.getAddressId());
      shipment.setAccount(account);
      shipment.setAddress(address);
    }
    return shipment;
  }

  public ShipmentDisplay retrieveShipmentDates(long id) {
    return shipmentRepository.retrieveShipmentDates(id);
//    return shipment;
  }

  public List<ShipmentDisplay> retrieveAccountShipments(long accountId) {
    List<Shipment> shipmentList = shipmentRepository.findByAccountId(accountId);
    List<ShipmentDisplay> shipmentDisplayList = new ArrayList<>();
    for(Shipment shipment: shipmentList) {
      ShipmentDisplay shipmentDisplay = new ShipmentDisplay(shipment.getShippedDate(), shipment.getDeliveryDate());
      shipmentDisplay.setOrderNumber(retrieveOrderNumber(accountId));
      shipmentDisplay.setOrderLineItems(retrieveOrderLineDisplay(shipment.getId()));
      shipmentDisplayList.add(shipmentDisplay);
    }
    return shipmentDisplayList;
  }

  private Account retrieveAccount(long id) { return accountService.retrieveAccount(id); }
  private Address retrieveAddress(long accountId, long id) { return addressService.retrieveAddress(accountId, id); }
  private long retrieveOrderNumber(long accountId) { return ordersService.retrieveOrderNumber(accountId); }
  private List<OrderLineDisplay> retrieveOrderLineDisplay(long ordersId ) { return orderLineItemsService.retrieveShipmentDisplay(ordersId); }

}

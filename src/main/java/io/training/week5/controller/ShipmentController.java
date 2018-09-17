package io.training.week5.controller;

import io.training.week5.entity.Shipment;
import io.training.week5.model.ShipmentDisplay;
import io.training.week5.service.ShipmentService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ShipmentController {

  private ShipmentService shipmentService;

  public ShipmentController(ShipmentService shipmentService) {
    this.shipmentService = shipmentService;
  }

  @GetMapping("/{id}")
  public Shipment retrieveShipment(@PathVariable("id") long id) {
    return shipmentService.retrieveShipment(id);
  }


  @GetMapping("/{id}/dates")
  public ShipmentDisplay retrieveShipmentDates(@PathVariable("id") long id) {
    return shipmentService.retrieveShipmentDates(id);
  }

  @GetMapping
  public List<ShipmentDisplay> retrieveAccountShipments(@RequestParam("accountId") long accountId) {
    return shipmentService.retrieveAccountShipments(accountId);
  }

  @PostMapping
  public void addShipment(@RequestBody Shipment shipment) {
    shipmentService.addShipment(shipment);
  }

}

package io.training.week5.controller;

import io.training.week5.entity.Shipment;
import io.training.week5.model.ShipmentDisplay;
import io.training.week5.service.ShipmentService;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  @GetMapping("/{shipmentId}")
  public Shipment retrieveShipment(@PathVariable("shipmentId") long shipmentId) {
    return shipmentService.retrieveShipment(shipmentId);
  }

  @GetMapping("/{id}/dates")
  public ShipmentDisplay retrieveShipmentDates(@PathVariable("id") long id) {
    System.out.println("Finding shipment dates for shipmentId..." + id);
    return shipmentService.retrieveShipmentDates(id);
  }

  @GetMapping
  public List<ShipmentDisplay> retrieveAccountShipments(@RequestParam("accountId") long accountId) {
    return shipmentService.retrieveAccountShipments(accountId);
  }

  @PutMapping("/{shipmentId}")
  public Shipment updateShipment(@PathVariable("shipmentId") long shipmentId, @RequestBody Shipment shipment) {
    return shipmentService.updateShipment(shipmentId, shipment);
  }

  @PostMapping
  public Shipment addShipment(@RequestBody Shipment shipment) {
    return shipmentService.addShipment(shipment);
  }

  @Transactional
  @DeleteMapping("/{shipmentId}")
  public boolean removeShipment(@PathVariable("shipmentId") long shipmentId) {
    return shipmentService.removeShipment(shipmentId);
  }


}

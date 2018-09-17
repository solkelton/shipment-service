package io.training.week5.service;


import io.training.week5.model.OrderLineDisplay;
import io.training.week5.model.ShipmentDisplay;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient(name="order-service")
public interface OrderLineItemsService {

  @RequestMapping(method= RequestMethod.GET, value="/lines/{shipmentId}")
  public List<OrderLineDisplay> retrieveShipmentDisplay(@PathVariable("shipmentId") long orderId);

}

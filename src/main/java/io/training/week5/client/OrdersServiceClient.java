package io.training.week5.client;

import io.training.week5.model.OrderNumber;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient(name="order-service")
public interface OrdersServiceClient {

  @RequestMapping(method= RequestMethod.GET, value="/{accountId}/orderNumber")
  public List<OrderNumber> retrieveOrderNumber(@PathVariable("accountId") long accountId);

}

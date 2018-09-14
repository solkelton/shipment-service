package io.training.week5.service;

import io.training.week5.model.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name="order-service")
public interface OrdersService {

  @RequestMapping(method= RequestMethod.GET, value="orders/{accountId}/orderNumber")
  public long retrieveOrderNumber(@PathVariable("accountId") long accountId);

}

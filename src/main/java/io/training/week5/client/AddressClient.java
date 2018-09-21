package io.training.week5.client;

import io.training.week5.model.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient(name="account-service")
public interface AddressClient {

  @RequestMapping(method= RequestMethod.GET, value="/{accountId}/address/{addressId}")
  public Address retrieveAddress(@PathVariable("accountId") long accountId,
      @PathVariable("addressId") long addressId);
}

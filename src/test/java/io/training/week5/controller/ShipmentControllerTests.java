package io.training.week5.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.training.week5.entity.Shipment;
import io.training.week5.model.Account;
import io.training.week5.model.Address;
import io.training.week5.model.OrderLineDisplay;
import io.training.week5.model.ShipmentDisplay;
import io.training.week5.service.ShipmentService;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.tomcat.jni.Local;
import org.assertj.core.api.ZonedDateTimeAssert;
import org.hamcrest.Matchers;
import org.hibernate.criterion.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ShipmentController.class)
@AutoConfigureMockMvc(secure=false)
public class ShipmentControllerTests {

  @Autowired private MockMvc mockMvc;
  @MockBean private ShipmentService shipmentService;

  private Shipment shipment;
  private ShipmentDisplay shipmentDisplay;
  private List<ShipmentDisplay> shipmentDisplayList;
//  private LocalDateTime shippedDate;

  @Before
  public void setUpShipment() {
//    shippedDate = getShippedDate();
    shipment = createTestingShipment();
    shipmentDisplay = createTestingShipmentDisplay();
    shipmentDisplayList = createTestingShipmentDisplayList();
  }

  @Test
  public void testRetrieveShipment_ValidInput_ShouldReturnFoundShipmentEntry() throws Exception {
    when(shipmentService.retrieveShipment(1)).thenReturn(shipment);

    mockMvc.perform(get("/{id}",1))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.*", hasSize(4)))
        .andExpect(jsonPath("$.shippedDate", Matchers.anything()))
        .andExpect(jsonPath("$.account.firstName", Matchers.is("Nick")));

    verify(shipmentService, times(1)).retrieveShipment(1);
    verifyNoMoreInteractions(shipmentService);
  }

  @Test
  public void testRetrieveShipmentDates_ValidInput_ShouldReturnFoundShipmentDisplayEntry() throws Exception {
    when(shipmentService.retrieveShipmentDates(1)).thenReturn(shipmentDisplay);

    mockMvc.perform(get("/{id}/dates", 1))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.*", hasSize(4)))
        .andExpect(jsonPath("$.orderNumber", Matchers.is(123)));

    verify(shipmentService, times(1)).retrieveShipmentDates(1);
    verifyNoMoreInteractions(shipmentService);
  }

  @Test
  public void testRetrieveAccountShipments_ValidInput_ShouldReturnArrayShipmentDisplay() throws Exception {
    when(shipmentService.retrieveAccountShipments(1)).thenReturn(shipmentDisplayList);
    mockMvc.perform(get("/shipments")
        .param("accountId","1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$.*", hasSize(3)))
        .andExpect(jsonPath("$[0].orderNumber", Matchers.is(123)));

    verify(shipmentService, times(1)).retrieveAccountShipments(1);
    verifyNoMoreInteractions(shipmentService);
  }

  private Shipment createTestingShipment() {
    Shipment shipment = new Shipment(1L, 1L, LocalDateTime.now(), LocalDateTime.now());
    Address shippingAddress = new Address(1L, "18", "E. Elm Street", "Chicago", "IL", "60611","US");
    Account account = new Account("Nick", "Kelton","nick@gmail.com");
    shipment.setAddress(shippingAddress);
    shipment.setAccount(account);
    shipment.setId(1L);
    return shipment;
  }

  private ShipmentDisplay createTestingShipmentDisplay() {
    ShipmentDisplay shipmentDisplay = new ShipmentDisplay(LocalDateTime.now(), LocalDateTime.now());
    List<OrderLineDisplay> orderLineDisplayList = new ArrayList<OrderLineDisplay>(){{
      add(new OrderLineDisplay("product1", 1));
      add(new OrderLineDisplay("product2",2));
    }};
    shipmentDisplay.setOrderLineItems(orderLineDisplayList);
    shipmentDisplay.setOrderNumber(123);
    return shipmentDisplay;
  }

  private List<ShipmentDisplay> createTestingShipmentDisplayList(){
    return new ArrayList<ShipmentDisplay>() {{
      add(shipmentDisplay);
      add(shipmentDisplay);
      add(shipmentDisplay);
    }};
  }

//  private LocalDateTime getShippedDate() {
//    String shippedDate = "2018-11-28 12:00:00";
//    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//    return LocalDateTime.parse(shippedDate, format);
//  }

}

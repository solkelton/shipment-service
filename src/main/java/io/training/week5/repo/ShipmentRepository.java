package io.training.week5.repo;

import io.training.week5.entity.Shipment;
import io.training.week5.model.ShipmentDisplay;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

  @Query(nativeQuery=true, name="retrieveShipmentDates")
  ShipmentDisplay retrieveShipmentDates(long shipmentId);

  @Nullable
  List<Shipment> findAllByAccountId(long accountId);

  @Nullable
  Shipment getShipmentById(long shipmentId);

  void deleteShipmentById(long shipmentId);

}



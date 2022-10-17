package com.mosaicsquare.notification.websocket.database.sale;

import com.mosaicsquare.notification.websocket.database.sale.dto.SaleOffer;
import org.springframework.data.repository.CrudRepository;

public interface SaleOfferRepository extends CrudRepository<SaleOffer, Long> {
}

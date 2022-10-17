package com.mosaicsquare.notification.websocket.database.sale;

import com.mosaicsquare.notification.websocket.database.sale.dto.SaleInfo;
import org.springframework.data.repository.CrudRepository;

public interface SaleInfoRepository extends CrudRepository<SaleInfo, Long> {
}

package com.mosaicsquare.notification.websocket.database.sale.dto;

import com.mosaicsquare.notification.websocket.database.nft.dto.Nft;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="sale_offer")
public class SaleOffer {
    public enum OfferStatus {
        offered, dismissed, accepted, successful_bid, refund_pending, refunded;
    }

    public enum TxStatus {
        complete, pending, reject;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="sale_info_id", nullable = false)
    private Long saleInfoId;

    @Column(name="offer_member_id", nullable = false)
    private Long offerMemberId;

    @Column(name="price", nullable = false, columnDefinition="DECIMAL(27,18)")
    private BigDecimal price;

    @Column(name="currency_type", columnDefinition="TINYINT")
    private Integer currencyType;

    @Column(name="tx_hash", nullable = false)
    private String txnHash;

    @Column(name="tx_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TxStatus txnStatus;

    @Column(name="status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OfferStatus status;

    @Column(name="created_date", nullable = false)
    private Timestamp createdDate;

    @Column(name="updated_date", nullable = false)
    private Timestamp updatedDate;
}

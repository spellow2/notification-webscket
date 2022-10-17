package com.mosaicsquare.notification.websocket.database.sale.dto;

import com.mosaicsquare.notification.websocket.database.nft.dto.Nft;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="sale_info")
public class SaleInfo {
    public enum SalesInfoStatus {
        Ready, InProgress, Cancelled, Expired, Sold;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nft_id", nullable = false)
    private Long nftId;

    @Column(name="seller_member_id", nullable = false)
    private Long sellerMemberId;

    @Column(name="sale_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Nft.SalesType saleType;

    @Column(name="price", nullable = false, columnDefinition="DECIMAL(27,18)")
    private BigDecimal price;

    @Column(name="currency_type", columnDefinition="TINYINT")
    private Integer currencyType;

    @Column(name="start_date", nullable = false)
    private Timestamp startDate;

    @Column(name="end_date", nullable = false)
    private Timestamp endDate;

    @Column(name="status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SalesInfoStatus status;

    @Column(name="tx_hash", nullable = false)
    private String txHash;

    @Column(name="created_date", nullable = false)
    private Timestamp createdDate;

    @Column(name="updated_date", nullable = false)
    private Timestamp updatedDate;
}

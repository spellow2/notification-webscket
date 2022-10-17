package com.mosaicsquare.notification.websocket.database.nft.dto;

import com.mosaicsquare.notification.websocket.database.sale.dto.SaleInfo;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name="nft")
public class Nft {
    public enum SalesType {
        on_sale, on_offer, on_auction;
    }

    @OneToMany
    @JoinColumn(name="nft_id")
    private List<NftLike> listNftLike;

    @OneToMany
    @JoinColumn(name="nft_id")
    private List<SaleInfo> listSaleInfo;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="address", nullable = false,length = 64)
    private String address;

    @Column(name="title", nullable = false,length = 128)
    private String title;

    @Column(name="description", columnDefinition="TEXT")
    private String description;

    @Column(name="token_id")
    private Integer tokenId;

    @Column(name="metadata_ipfs_url", nullable = false, columnDefinition="TEXT")
    private String metadataIpfsUrl;

    @Column(name="file_ipfs_url", nullable = false, length = 2048)
    private String fileIpfsUrl;

    @Column(name="owner_id")
    private Long ownerId;

    @Column(name="thumbnail_image_location", length = 2048)
    private String thumbnailImageLocation;

    @Column(name="property", columnDefinition="TEXT")
    private String property;

    @Column(name="price", columnDefinition="DECIMAL(27,18)")
    private BigDecimal price;

    @Column(name="price_last", columnDefinition="DECIMAL(27,18)")
    private BigDecimal priceLast;

    @Column(name="blockchain_type", nullable = false, length = 16)
    private String blockchainType;

    @Column(name="buy_now_available", columnDefinition="BIT(1)")
    private Boolean buyNowAvailable;

    @Column(name="sales_status")
    @Enumerated(EnumType.STRING)
    private SalesType salesStatus;

    @Column(name="minted_date")
    private Timestamp mintedDate;

    @Column(name="collection_id")
    private Long collectionId;

    @Column(name="gallery_id")
    private Long galleryId;

    @Column(name="market_placed", columnDefinition="BIT(1)")
    private Boolean marketPlaced;

    @Column(name="created_date", nullable = false)
    private Timestamp createDate;

    @Column(name="update_date", nullable = false)
    private Timestamp updateDate;
}

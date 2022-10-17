package com.mosaicsquare.notification.websocket.database.nft.dto;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="nft_like")
public class NftLike {

    public NftLike() {
    }

    public NftLike(Long nftId, Long memberId) {
        setNftId(nftId);
        setMemberId(memberId);
        setCreatedDate(new Timestamp(System.currentTimeMillis()));
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nft_id")
    private Long nftId;

    @Column(name="member_id", nullable = false)
    private Long memberId;

    @Column(name="created_date", nullable = false)
    private Timestamp createdDate;
}

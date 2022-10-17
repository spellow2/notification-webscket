package com.mosaicsquare.notification.websocket.database.member.dto;

import com.mosaicsquare.notification.websocket.database.nft.dto.Nft;
import com.mosaicsquare.notification.websocket.database.nft.dto.NftLike;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name="member_info",uniqueConstraints = {@UniqueConstraint(name = "WALLET_ADDRESS_UNIQUE", columnNames = {"wallet_address"})})
public class MemberInfo {
    @OneToMany
    @JoinColumn(name="to_member_id")
    private List<MemberFollow> listFollower;

    @OneToMany
    @JoinColumn(name="owner_id")
    private List<Nft> listOwnNft;

    @OneToMany
    @JoinColumn(name="member_id")
    private List<NftLike> listLikeNft;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="wallet_address", nullable = false,length = 64)
    private String walletAddress;

    @Column(name="blockchain_type", nullable = false,length = 16)
    private String blockchainType;

    @Column(name="refresh_token", length = 512)
    private String refreshToken;

    @Column(name="nonce", nullable = false,length = 64)
    private String nonce;

    @Column(name="member_type", nullable = false,length = 16)
    private String memberType;

    @Column(name="creator_type", nullable = false,length = 16)
    private String creatorType;

    @Column(name="creator_state", nullable = false,length = 16)
    private String creatorState;

    @Column(name="mobile_number", length = 16)
    private String mobileNumber;

    @Column(name="username", length = 30)
    private String userName;

    @Column(name="email_address", length = 320)
    private String emailAddress;

    @Column(name="instagram", length = 320)
    private String instagram;

    @Column(name="twitter", length = 320)
    private String twitter;

    @Column(name="website", length = 320)
    private String website;

    @Column(name="open_owner_nft_list", columnDefinition="BIT(1)")
    private Boolean openOwnerNftList;

    @Column(name="open_created_nft_list", columnDefinition="BIT(1)")
    private Boolean openCreatedNftList;

    @Column(name="profile_image_location", columnDefinition="TEXT")
    private String profileImageLocation;

    @Column(name="description", length = 512)
    private String description;

    @Column(name="last_login_date")
    private Timestamp lastLoginDate;

    @Column(name="create_date")
    private Timestamp createDate;

    @Column(name="update_date")
    private Timestamp updateDate;
}

package com.mosaicsquare.notification.websocket.database.member;

import com.mosaicsquare.notification.websocket.database.member.dto.MemberInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MemberInfoRepository extends CrudRepository<MemberInfo, Long> {

    public MemberInfo findMemberInfoById(Long memberId);
    public MemberInfo findMemberInfoByWalletAddress(String walletAddress);
    public List<MemberInfo> findMemberInfosByMemberType(String memberType);
    /*
    //@Query(value="select mi.* from member_info mi where mi.id = ?1", nativeQuery=true)
    public MemberInfo getMemberInfoById(Long memberId);

    //@Query(value="select mi.* from member_info mi , member_follow mf where mi.id = mf.from_member_id and mf.to_member_id = ?1", nativeQuery=true)
    public List<MemberInfo> getFollowerInfosById(Long memberId);

    //@Query(value="select mi.* from member_info mi , nft n where mi.id = n.owner_id and n.id = ?1", nativeQuery=true)
    public MemberInfo getOwnerInfoByNftId(Long nftId);

    //@Query(value="select mi.* from member_info mi , nft_like nl where mi.id = nl.member_id and nl.nft_id = ?1", nativeQuery=true)
    public List<MemberInfo> getLikerInfosByNftId(Long nftId);

    //@Query(value="select mi.* from member_info mi , auction_bid ab , on_auction oa where mi.id = ab.bidder_member_id and ab.auction_id = oa.id and oa.nft_id = ?1", nativeQuery=true)
    public List<MemberInfo> getAuctionerInfosByNftId(Long nftId);

    //@Query(value="select mi.* from member_info mi , nft n where mi.id = n.owner_id and n.id = ?1", nativeQuery=true)
    public List<MemberInfo> getOfferInfosByNftId(Long nftId);


    //public MemberInfo getMemberInfoById(Long id);
    public MemberInfo getMemberInfoByWalletAddress(String walletAddress);
    */
}

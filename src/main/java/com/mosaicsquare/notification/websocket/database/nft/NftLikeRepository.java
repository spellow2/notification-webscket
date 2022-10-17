package com.mosaicsquare.notification.websocket.database.nft;

import com.mosaicsquare.notification.websocket.database.nft.dto.NftLike;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NftLikeRepository extends CrudRepository<NftLike, Long> {
    public List<NftLike> findNftLikesByNftId(Long nftId);
    public List<NftLike> findNftLikesByMemberId(Long memberId);
}
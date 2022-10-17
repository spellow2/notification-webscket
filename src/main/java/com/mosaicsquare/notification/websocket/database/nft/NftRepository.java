package com.mosaicsquare.notification.websocket.database.nft;

import com.mosaicsquare.notification.websocket.database.nft.dto.Nft;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NftRepository extends CrudRepository<Nft, Long> {
    public Nft findNftById(Long nftId);
    public List<Nft> findNftsByOwnerId(Long ownerId);
}
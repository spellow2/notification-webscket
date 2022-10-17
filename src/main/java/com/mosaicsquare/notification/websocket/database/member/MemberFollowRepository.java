package com.mosaicsquare.notification.websocket.database.member;

import com.mosaicsquare.notification.websocket.database.member.dto.MemberFollow;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MemberFollowRepository extends CrudRepository<MemberFollow, Long> {
    public List<MemberFollow> findMemberFollowsByFollowFromMemberId(Long fromId);
    public List<MemberFollow> findMemberFollowsByFollowToMemberId(Long toId);
}
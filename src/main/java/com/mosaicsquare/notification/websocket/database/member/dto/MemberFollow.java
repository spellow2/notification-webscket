package com.mosaicsquare.notification.websocket.database.member.dto;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="member_follow")
public class MemberFollow {

    public MemberFollow() {
    }

    public MemberFollow(Long to, Long from) {
        setFollowToMemberId(to);
        setFollowFromMemberId(from);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="to_member_id")
    private Long followToMemberId;

    @Column(name="from_member_id")
    private Long followFromMemberId;
}

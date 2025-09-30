package com.eat.today.sns.command.application.entity.dm;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="direct_message")
public class DmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="message_no")
    private int messageNo;

    @Column(name="send_member_id", nullable = false)
    private int sendMemberId;

    @Column(name="receive_member_id", nullable = false)
    private int receiveMemberId;

    @Column(name="dm_content", nullable = false)
    private String dmContent;

    @Column(name="dm_date", nullable = false)
    private String dmDate;

    @Column(name = "dm_read", nullable = false)
    private Boolean dmRead;
}
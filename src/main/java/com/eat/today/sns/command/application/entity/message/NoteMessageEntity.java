package com.eat.today.sns.command.application.entity.message;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="note_message")
@Data
public class NoteMessageEntity {
    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Column(name = "note_id", nullable = false)
    private Integer noteId;

    @Column(name = "sender_no", nullable = false)
    private Integer senderNo;

    @Column(name = "receiver_no", nullable = false)
    private Integer receiverNo;

    @Column(name = "subject", nullable = false, length = 255)
    private String  subject;

    @Column(name = "content", nullable = false, length = 255)
    private String content;

    @Column(name = "sent_at_txt", nullable = false, length = 255)
    private String  sentAtTxt;

    @Column(name = "is_read", nullable = false)
    @ColumnDefault("0")
    private boolean isRead;

    @Column(name = "read_at_txt", nullable = false, length = 255)
    private String  readAtTxt;

    @Column(name = "reply_to_id", nullable = false)
    private Integer replyToId;

    @Column(name = "sender_deleted", nullable = false)
    @ColumnDefault("0")
    private boolean senderDeleted;

    @Column(name = "receiver_deleted", nullable = false)
    @ColumnDefault("0")
    private boolean receiverDeleted;
}


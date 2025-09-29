package com.eat.today.post.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alcohol")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alcohol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alcohol_no")
    private Integer alcoholNo;

    @Column(name = "alcohol_type", nullable = false)
    private String alcoholType;

    @Column(name = "alcohol_explain", columnDefinition = "TEXT")
    private String alcoholExplain;

    @Column(name = "alcohol_picture")
    private String alcoholPicture;

    public void update(String type, String explain, String picture){
        this.alcoholType = type;
        this.alcoholExplain = explain;
        this.alcoholPicture = picture;
    }

}

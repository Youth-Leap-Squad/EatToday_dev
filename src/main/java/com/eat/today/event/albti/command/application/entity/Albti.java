package com.eat.today.event.albti.command.application.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "albti")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Albti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alBTI_no", nullable = false)
    private int albtiNo;

    @Column(name = "alBTI_category", nullable = false)
    private String albtiCategory;

    @Column(name = "alBTI_detail", nullable = false)
    private String albtiDetail;

    @Column(name = "alcohol_no", nullable = false)
    private int alcoholNo;

}

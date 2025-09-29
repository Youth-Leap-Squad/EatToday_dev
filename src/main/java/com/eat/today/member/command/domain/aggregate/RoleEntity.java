package com.eat.today.member.command.domain.aggregate;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@Table(name = "role")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_no" ,nullable = false)
    private Long roleNo;

    @Column(name = "role_name", nullable = false)
    private String roleName;

}

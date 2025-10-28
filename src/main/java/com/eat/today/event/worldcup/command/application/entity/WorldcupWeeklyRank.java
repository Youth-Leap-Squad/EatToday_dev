//package com.eat.today.event.worldcup.command.application.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table(name = "worldcup_weekly_rank",
//        uniqueConstraints = @UniqueConstraint(columnNames = {"week_no", "alcohol_id", "food_id"}))
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class WorldcupWeeklyRank {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @Column(name = "week_no", nullable = false)
//    private int weekNo;
//
//    @Column(name = "alcohol_id", nullable = false)
//    private int alcoholId;
//
//    @Column(name = "food_id", nullable = false)
//    private int foodId;
//
//    @Column(name = "win_count", nullable = false)
//    private int winCount;
//}
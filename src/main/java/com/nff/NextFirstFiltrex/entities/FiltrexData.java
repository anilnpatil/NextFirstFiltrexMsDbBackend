package com.nff.NextFirstFiltrex.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "filtrex_data", schema = "dbo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FiltrexData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "production_count")
    private Integer productionCount;

    @Column(name = "status")
    private Integer status;

    @Column(name = "air_flow_test_result")
    private String airFlowTestResult;

    @Column(name = "final_assembly_height")
    private Double finalAssemblyHeight;

    @Column(name = "top_cap_press_time")
    private Double topCapPressTime;

    @Column(name = "top_cap_hold_time")
    private Double topCapHoldTime;

    @Column(name = "bottom_cap_press_time")
    private Double bottomCapPressTime;

    @Column(name = "bottom_cap_hold_time")
    private Double bottomCapHoldTime;

    @Column(name = "child_part_refill_status")
    private String childPartRefillStatus;

    @Column(name = "sku")
    private String sku;   // 🔥 lowercase variable name

    @Column(name = "shift")
    private Integer shift;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "cycle_time")
    private Double cycleTime; // seconds
}

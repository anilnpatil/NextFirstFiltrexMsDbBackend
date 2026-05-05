package com.nff.NextFirstFiltrex.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "filtrex_data", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FiltrexData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sl_no")
    private Long slNo;   // use Long instead of Integer (better for large tables)

    @Column(name = "sku")
    private Integer sku;

    @Column(name = "shift")
    private Integer shift;

    @Column(name = "top_cap_press_and_hold_time")
    private Integer topCapPressAndHoldTime;

    @Column(name = "bottom_cap_press_and_hold_time")
    private Integer bottomCapPressAndHoldTime;

    @Column(name = "block_height_value")
    private Float blockHeightValue;   

    @Column(name = "block_height_inspection_status")
    private Integer blockHeightInspectionStatus;

    @Column(name = "air_flow_test_result")
    private Integer airFlowTestResult;

    @Column(name = "part_status")
    private Integer partStatus;

    @Column(name = "cycle_time")
    private Float cycleTime;

    @Column(name = "production_date_time")
    private LocalDateTime productionDateTime;

    // computed column
    @Column(name = "production_date", insertable = false, updatable = false)
    private LocalDate productionDate;

    @Column(name = "cloth_refill_status")
    private Integer clothRefillStatus;

    @Column(name = "cap_refill_status")
    private Integer capRefillStatus;

    @Column(name = "glue_refill_status")
    private Integer glueRefillStatus;
}
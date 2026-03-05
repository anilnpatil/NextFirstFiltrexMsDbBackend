package com.nff.NextFirstFiltrex.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nff.NextFirstFiltrex.entities.FiltrexData;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface FiltrexDataRepository extends JpaRepository<FiltrexData, Long> {

    FiltrexData findFirstByOrderByTimestampDesc();

    List<FiltrexData> findByShift(int shift);

    List<FiltrexData> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

        @Query("SELECT f FROM FiltrexData f WHERE f.timestamp BETWEEN :start AND :end AND (:shift IS NULL OR f.shift = :shift) AND (:sku IS NULL OR f.sku = :sku)")
        Page<FiltrexData> findByTimestampBetweenAndShiftOptional(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("shift") Integer shift,
            @Param("sku") String sku,
            Pageable pageable
        );


    @Query("SELECT f FROM FiltrexData f WHERE YEAR(f.timestamp) = :year AND MONTH(f.timestamp) = :month")
    List<FiltrexData> findByMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT f FROM FiltrexData f WHERE YEAR(f.timestamp) = :year AND WEEK(f.timestamp) = :week")
    List<FiltrexData> findByWeek(@Param("year") int year, @Param("week") int week);

}

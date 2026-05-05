package com.nff.NextFirstFiltrex.services.impl;

import com.nff.NextFirstFiltrex.dto.RejectionTypeProjection;
import com.nff.NextFirstFiltrex.repositories.RejectionGraphRepository;
import com.nff.NextFirstFiltrex.services.RejectionGraphService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RejectionGraphServiceImpl implements RejectionGraphService {

    private final RejectionGraphRepository repository;

    private static Integer normalize(Integer v) {
        return (v != null && v == 0) ? null : v;
    }

    @Override
    public List<RejectionTypeProjection> getDaily(
            LocalDate from, LocalDate to, Integer sku, Integer shift) {

        return repository.getDaily(
                from.toString(),
                to.toString(),
                normalize(sku),
                normalize(shift)
        );
    }

    @Override
    public List<RejectionTypeProjection> getWeekly(
            int year, Integer sku, Integer shift) {

        return repository.getWeekly(
                year,
                normalize(sku),
                normalize(shift)
        );
    }

    @Override
    public List<RejectionTypeProjection> getMonthly(
            int year, Integer sku, Integer shift) {

        return repository.getMonthly(
                year,
                normalize(sku),
                normalize(shift)
        );
    }
}
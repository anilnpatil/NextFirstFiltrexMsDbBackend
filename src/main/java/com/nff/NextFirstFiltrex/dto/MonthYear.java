package com.nff.NextFirstFiltrex.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class MonthYear {
        public int month;
        public int year;

        public MonthYear(int month, int year) {
            this.month = month;
            this.year = year;
        }
    }
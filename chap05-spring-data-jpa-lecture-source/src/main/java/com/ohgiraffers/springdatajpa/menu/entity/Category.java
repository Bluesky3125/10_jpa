package com.ohgiraffers.springdatajpa.menu.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Category {
    private int categoryCode;
    private String categoryName;
    private Integer refCategoryCode;
}
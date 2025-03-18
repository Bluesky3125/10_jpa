package com.ohgiraffers.springdatajpa.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ohgiraffers.springdatajpa.menu.entity.Menu;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findByMenuPriceGreaterThan(int menuPrice);
}

package com.ohgiraffers.springdatajpa.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ohgiraffers.springdatajpa.menu.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
}

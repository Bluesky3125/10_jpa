package com.ohgiraffers.springdatajpa.menu.service;

import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.repository.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ohgiraffers.springdatajpa.menu.entity.Menu;

@Service
@Slf4j
public class MenuService {

    private final MenuRepository menuRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public MenuDTO findMenuByCode(int menuCode) {

        Menu menu = menuRepository.findById(menuCode).get();    // get 없으면 Optional로 반환됨, orElse~로 Exception 발생시킬 수 있음
        log.debug("menu: {}", menu);

        return null;
    }
}

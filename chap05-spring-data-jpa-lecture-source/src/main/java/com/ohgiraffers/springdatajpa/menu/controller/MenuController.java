package com.ohgiraffers.springdatajpa.menu.controller;

import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu")
@Slf4j
public class MenuController {

    private final MenuService menuService;

    /* 설명.
     *  Logger를 활용한 로그 생성 이유?
     *   1. println보다 성능적으로 우수하다.
     *   2. 외부 리소스 파일로 따로 저장 및 송출이 가능하다.
     *   3. 로그 레벨에 따른 확인이 가능하다. (개발: debug, 서비스: info)
     * */

//    Logger logger = LoggerFactory.getLogger(MenuController.class);
//    Logger logger = LoggerFactory.getLogger(getClass());  Lombok 있을 시 @Slf4j 어노테이션으로 대체 가능

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/{menuCode}")
    public String findMenuByCode(@PathVariable int menuCode, Model model) {
//        System.out.println("menuCode = " + menuCode); log로 바꾸기
//        logger.debug("menuCode = {}", menuCode);
        log.debug("menuCode = {}", menuCode);

        MenuDTO menu = menuService.findMenuByCode(menuCode);

        model.addAttribute("menu", menu);

        return "menu/detail";
    }
}

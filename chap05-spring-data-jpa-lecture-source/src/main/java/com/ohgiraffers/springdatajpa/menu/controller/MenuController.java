package com.ohgiraffers.springdatajpa.menu.controller;

import com.ohgiraffers.springdatajpa.common.Pagination;
import com.ohgiraffers.springdatajpa.common.PagingButtonInfo;
import com.ohgiraffers.springdatajpa.menu.dto.CategoryDTO;
import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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

    /* 설명. 페이징 처리 전 */
//    @GetMapping("/list")
//    public String findMenuList(Model model) {
//        List<MenuDTO> menuList = menuService.findMenuList();
//        model.addAttribute("menuList", menuList);
//
//        return "menu/list";
//    }

    /* 설명. 페이징 처리 후 */
    /* 설명.
     *  @PageableDefault
     *  1. 기본 한 페이지에 10개의 데이터(size, value)
     *  2. 기본 1페이지부터(0부터)
     *  3. 기본 오름차순(ASC)
     * */
    @GetMapping("/list")
    public String findMenulist(@PageableDefault Pageable pageable, Model model) {   // Pageable 객체는 Spring Data가 있어야 사용 가능!
        log.debug("pageable = {}", pageable);

        Page<MenuDTO> menuList = menuService.findMenuList(pageable);

        log.debug("조회한 내용 목록: {}", menuList.getContent());
        log.debug("총 페이지 수: {}", menuList.getTotalPages());
        log.debug("총 메뉴 수: {}", menuList.getTotalElements());
        log.debug("해당 페이지에 표시될 요소 수: {}", menuList.getSize());
        log.debug("해당 페이지의 실제 요소 수: {}", menuList.getNumberOfElements());
        log.debug("Page의 number가 처음이면(첫 페이지면): {}", menuList.isFirst());
        log.debug("Page의 number가 마지막이면(마지막 페이지면): {}", menuList.isLast());
        log.debug("현재 페이지: {}", menuList.getNumber());
        log.debug("정렬 기준: {}", menuList.getSort());


        /* 설명. Page 객체를 통해 PagingButtonInfo 추출 */
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(menuList);

        model.addAttribute("menuList", menuList);
        model.addAttribute("paging", paging);

        return "menu/list";
    }

    @GetMapping("querymethod")
    public void queryMethodPage() {}

    @GetMapping("search")
    public String findMenuSearch(@RequestParam/* 명시적으로 작성 */ int menuPrice, Model model) {
        List<MenuDTO> menuList = menuService.findMenuPrice(menuPrice);

        model.addAttribute("menuList", menuList);
        model.addAttribute("menuPrice", menuPrice);
        return "menu/searchResult";
    }

    @GetMapping("regist")
    public void registMenuPage() {}

    @GetMapping("category")
    @ResponseBody
    public List<CategoryDTO> findCategoryList() {
        return menuService.findAllCategory();
    }

    @PostMapping("regist")
    public String registMenu(MenuDTO newMenu) {
        log.info("컨트롤러에서 커맨드 객체로 한 번에 입력값 잘 받는지 확인: {}", newMenu);

        menuService.registMenu(newMenu);

        return "redirect:/menu/list";
    }

    @GetMapping("modify")
    public void modifyMenuPage() {}

    @PostMapping("modify")
    public String modifyMenu(MenuDTO modifyMenu) {
        menuService.modifyMenu(modifyMenu);

        return "redirect:/menu/" + modifyMenu.getMenuCode();
    }

    @GetMapping("delete")
    public void deleteMenuPage() {}

    @PostMapping("delete")
    public String deleteMenu(@RequestParam int menuCode) {

        menuService.deleteMenu(menuCode);

        return "redirect:/menu/list";
    }
}

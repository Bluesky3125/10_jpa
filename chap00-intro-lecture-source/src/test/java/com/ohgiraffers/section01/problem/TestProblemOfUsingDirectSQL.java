package com.ohgiraffers.section01.problem;

import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestProblemOfUsingDirectSQL {

    private Connection con;

    @BeforeEach
    void setConnection() throws ClassNotFoundException, SQLException {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/menudb";
        String user = "swcamp";
        String password = "swcamp";

        Class.forName(driver);

        con = DriverManager.getConnection(url, user, password);
        con.setAutoCommit(false);
    }

    @AfterEach
    void closeConnection() throws SQLException {
        con.rollback();
        con.close();
    }

    /* 목차. JDBC API를 이용해 직접 SQL을 다룰 때 발생할 수 있는 문제점
     *  1. 데이터 변환, SQL 작성, JDBC API 코드 등의 중복 작성(개발 시간 증가, 유지보수성 저하)
     *  2. SQL에 의존하여 개발
     *  3. 패러다임 불일치(상속, 연관관계, 객체 그래프 탐색, 방향성)
     *  4. 동일성 보장 문제
     * */

    /* 목차. 1 */
    @DisplayName("1. 직접 SQL을 작성하여 메뉴를 조회할 때 발생하는 문제 확인")
    /* 설명. Mybatis도 공유하는 문제, DB centric하게 작성되어 있어, 컬럼명 변경 시 같이 변경할 코드가 너무 많음. */
    @Test
    void testDirectSelectSql() throws SQLException {

        // given
        String query = "SELECT MENU_CODE, MENU_NAME, MENU_PRICE, CATEGORY_CODE, ORDERABLE_STATUS FROM TBL_MENU";

        // when
        Statement stmt = con.createStatement();
        ResultSet rSet = stmt.executeQuery(query);

        List<Menu> menuList = new ArrayList<>();
        while (rSet.next()) {
            Menu menu = new Menu();
            menu.setMenuCode(rSet.getInt("MENU_CODE"));
            menu.setMenuName(rSet.getString("MENU_NAME"));
            menu.setMenuPrice(rSet.getInt("MENU_PRICE"));
            menu.setCategoryCode(rSet.getInt("CATEGORY_CODE"));
            menu.setOrderableStatus(rSet.getString("ORDERABLE_STATUS"));

            menuList.add(menu);
        }

        // then
        Assertions.assertFalse(menuList.isEmpty());
        menuList.forEach(System.out::println);

        rSet.close();
        stmt.close();
    }

    /* 목차. 2 */
    @DisplayName("2. 연관된 객체 문제 확인")
    @Test
    void testAssociationObject() throws SQLException {

        // given
        String query = "SELECT A.MENU_CODE, A.MENU_NAME, A.MENU_PRICE, B.CATEGORY_CODE, B.CATEGORY_NAME, A.ORDERABLE_STATUS "
                     + "FROM TBL_MENU A "
                     + "JOIN TBL_CATEGORY B ON (A.CATEGORY_CODE = B.CATEGORY_CODE)";

        // when
        Statement stmt = con.createStatement();
        ResultSet rSet = stmt.executeQuery(query);

        List<MenuAndCategory> menuAndCategoryList = new ArrayList<>();
        while (rSet.next()) {
            MenuAndCategory menuAndCategory = new MenuAndCategory();
            menuAndCategory.setMenuCode(rSet.getInt("MENU_CODE"));
            menuAndCategory.setMenuName(rSet.getString("MENU_NAME"));
            menuAndCategory.setMenuPrice(rSet.getInt("MENU_PRICE"));
            menuAndCategory.setCategory(
                    new Category(
                            rSet.getInt("CATEGORY_CODE"),
                            rSet.getString("CATEGORY_NAME")
                    )
            );
            menuAndCategory.setOrderableStatus(rSet.getString("ORDERABLE_STATUS"));

            menuAndCategoryList.add(menuAndCategory);
        }

        // then
        Assertions.assertFalse(menuAndCategoryList.isEmpty());
        menuAndCategoryList.forEach(System.out::println);

        rSet.close();
        stmt.close();
    }

    /* 목차. 3. 패러다임 불일치(상속, 연관관계, 객체 그래프 탐색, 방향성) */
    /* 설명. 3-1. 상속 문제
     *  객체 지향 언어의 상속 개념과 유사한 것이 데이터베이스의 서브타입 엔티티이다.(서브타입을 별도의 클래스로 나뉘었을 때)
     *  슈퍼타입의 모든 속성을 서브타입이 공유하지 못하여 물리적으로 다른 테이블로 분리가 된 형태이다.
     *  (설계에 따라서는 하나의 테이블로 속성이 추가되기도 한다.)
     *  하지만 객체지향의 상속은 슈퍼타입의 속성을 공유해서 사용하므로 여기에서 패러다임의 불일치가 발생한다.
     * */

    /* 설명. 3-2. 연관관계 문제, 객체 그래프 탐색 문제, 방향성 문제
     *  객체지향에서 말하는 가지고 있는(ASSOCIATION 연관관계 혹은 COLLECTION 연관관계) 경우 데이터베이스 저장 구조와
     *  다른 형태이다.
     *  　
     *  - 데이터베이스 테이블에 맞춘 객체 모델
     *  public class Menu {
     *    private int menuCode;
     *    private String menuName;
     *    private int menuPrice;
     *    private int categoryCode;
     *    private String orderableStatus;
     *  }
     *  　
     *  - 객체 지향 언어에 맞춘 객체 모델
     *  public class Menu {
     *    private int menuCode;
     *    private String menuName;
     *    private int menuPrice;
     *    private Category category;
     *    private String orderableStatus;
     *  }
     * */

    /* 목차. 4. 동일성 보장 문제 */
    @DisplayName("4. 조회한 두 개의 행을 담은 객체의 동일성 비교 테스트")
    @Test
    void testEquals() throws SQLException {

        // given
        String query = "SELECT MENU_CODE, MENU_NAME FROM TBL_MENU WHERE MENU_CODE = 12";

        // when
        Statement stmt1 = con.createStatement();
        ResultSet rSet1 = stmt1.executeQuery(query);

        Menu menu1 = null;
        while (rSet1.next()) {
            menu1 = new Menu();
            menu1.setMenuCode(rSet1.getInt("MENU_CODE"));
            menu1.setMenuName(rSet1.getString("MENU_NAME"));
        }

        Statement stmt2 = con.createStatement();
        ResultSet rSet2 = stmt2.executeQuery(query);

        Menu menu2 = null;
        while (rSet2.next()) {
            menu2 = new Menu();
            menu2.setMenuCode(rSet2.getInt("MENU_CODE"));
            menu2.setMenuName(rSet2.getString("MENU_NAME"));
        }

        // then
        Assertions.assertNotEquals(menu1, menu2);

        /* 설명.
         *  JPA를 활용하면 동일 비교가 가능하다.
         *  Menu menu1 = entityManager.find(Menu.class, 1);
         *  Menu menu2 = entityManager.find(Menu.class, 1);
         *  System.out.println(menu1 == menu2);
         * */
    }
}

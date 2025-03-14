package com.ohgiraffers.section03.primarykey.subsection01.identity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PrimaryKeyMappingTest {
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initEntityManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @Test
    public void 식별자_매핑_테스트() {

        Member member01 = new Member();
//        member.setMemberNo(1);
        member01.setMemberId("user01");
        member01.setMemberPwd("pass01");
        member01.setNickname("홍길동");
        member01.setPhone("010-1234-5678");
        member01.setEmail("hong@gmail.com");
        member01.setAddress("서울특별시 서초구");
        member01.setEnrollDate(new java.util.Date());
        member01.setMemberRole("ROLE_MEMBER");
        member01.setStatus("Y");

        Member member02 = new Member();
//        member.setMemberNo(1);
        member02.setMemberId("user02");
        member02.setMemberPwd("pass02");
        member02.setNickname("유관순");
        member02.setPhone("010-3131-5678");
        member02.setEmail("yu@gmail.com");
        member02.setAddress("서울특별시 강남구");
        member02.setEnrollDate(new java.util.Date());
        member02.setMemberRole("ROLE_ADMIN");
        member02.setStatus("Y");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(member01);
        entityManager.persist(member02);

        transaction.commit();

        /* 설명. persist 당시에는 부여되지 않은 pk값으로 commit 이후 조회를 하면 가능할까? */
//        Member selectedMember01 = entityManager.find(Member.class, 1);
//        Member selectedMember02 = entityManager.find(Member.class, 2);
//        System.out.println("selectedMember01 = " + selectedMember01);
//        System.out.println("selectedMember02 = " + selectedMember02);
//
//        Assertions.assertEquals(1, selectedMember01.getMemberNo());
//        Assertions.assertEquals(2, selectedMember02.getMemberNo());

        /* 설명. 다중 행 조회는 find로는 안되고 jpql이라는 문법을 사용해야 가능하다. */
        String jpql = "SELECT A FROM member_section03_subsection01 A";
        List<Member> memberNoList = entityManager.createQuery(jpql, Member.class).getResultList();

        memberNoList.forEach(System.out::println);
    }
}

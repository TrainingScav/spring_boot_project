package com.tenco.blog.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import com.tenco.blog.utils.*;


@Data
// @Table : 실제 데이터베이스 테이블 명을 지정할 때 사용
@Table(name = "board_tb")
// @Entity : JPA가 이 클래스를 데이터베이스 테이블과 매핑하는 객체(엔티티)로 인식
// 즉, @Entity 어노테이션이 있어야 JPA가 이 객체를 관리 한다.
@Entity
public class Board {

    // @ID 이 필드가 기본키(primary key)임을 나타냄
    @Id
    // IDENTITY 전략 : 데이터베이스의 기본 전략을 사용한다. -> AUTO_INCREMENT (MySQL 방식)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 별도 어노테이션이 없으면 필드명이 컬럼명이 됨
    private String title;
    private String content;
    private String username;
    private Timestamp createdAt; // created_at(스네이크 케이스로 자동변환)

    // 머스태치에서 표현할 시간을 포맷기능을(행위) 스르로 만들자
    public String getTime() {
        return MyDateUtil.timestampFormat(createdAt);
    }
}

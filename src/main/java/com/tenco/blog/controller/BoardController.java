package com.tenco.blog.controller;

import com.tenco.blog.model.Board;
import com.tenco.blog.repository.BoardNativeRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller // IoC 대상 - 싱글톤 패턴으로 관리 됨
public class BoardController {

    private BoardNativeRepository boardNativeRepository;
    // DI : 의존성 주입 : 스프링이 자동으로 객체를 주입
    public BoardController(BoardNativeRepository boardNativeRepository) {
        this.boardNativeRepository = boardNativeRepository;
    }

    // 게시글 수정 처리
    @PostMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable(name = "id") Long id,
                             @RequestParam(name = "title") String title,
                             @RequestParam(name = "content") String content,
                             @RequestParam(name = "username") String username,
                             HttpServletRequest request) {

        boardNativeRepository.updateById(id, title, content, username);

        // PRG 패턴 적용
        // 수정 완료 후 해당 게시글 상세보기 페이지로 리다이렉트
        // 게시글 상세보기 URL
        return "redirect:/board/"+id;
    }

    // 게시글 수정 화면 요청 (GET 방식)
    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable(name = "id") Long id,
                             HttpServletRequest request) {

        Board board = boardNativeRepository.findById(id);
        request.setAttribute("board", board);

        return "board/update-form";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable(name = "id") Long id) {
        // 클라이언트 --> 삭제요청 처리 --> 응답 : 리다이렉트 처리 --> / 요청 --> 응답

        boardNativeRepository.deleteById(id);

        // PRG 패턴 (Post-Redirect-Get) 적용
        // 삭제 후 메인 페이지로 리다이렉트 하여 중복 삭제 방지
        // 새로 고침을 해도 삭제가 다시 실행되지 않음

        return "redirect:/";
    }

    /**
     * 상세보기 화면 요청
     * border/1
     */
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Long id, HttpServletRequest request){
        // URL에서 받은 id 값을 사용해서 특정 게시글 상세보기 조회
        // 실제로는 이 id 값으로 데이터베이스에 있는 게시글 조회하고
        // 머스테치 파일로 데이터를 내려 주어야 함(Model)

        Board board = boardNativeRepository.findById(id);
        request.setAttribute("board", board);

        return "board/detail";
    }

    @PostMapping("/board/save")
    // username, title, contect <-- DTO 받는 방법, 기본 데이터 타입 설정
    // form 태그에 넘어노는 데이터 받기;
    // form 태그에 name 속성에 key 값을 받기
    // http://localhost:8080/board/save
    public String save(@RequestParam("title") String title,
                       @RequestParam("content") String content,
                       @RequestParam("username") String username){
        System.out.println("title : " + title);
        System.out.println("contect : " + content);
        System.out.println("username : " + username);

        boardNativeRepository.save(title, content, username);
        return "redirect:/";
    }

    @GetMapping({"/","/index"})
    public String index(HttpServletRequest request){
        // DB 접근해서 select 결과값을 받아서 머스테치 파일에 데이터 바인딩 처리
        List<Board> boardList = boardNativeRepository.findAll();

        // 뷰에 데이터를 전달 -> Model 사용 가능
        request.setAttribute("boardList", boardList);

        return  "index";
    }

    @GetMapping("/board/save-form")
    public String saveFrom() {
        // prefix: /templates/
        return "board/save-form";
    }

}
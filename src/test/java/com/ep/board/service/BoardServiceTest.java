package com.ep.board.service;

import com.ep.board.dto.BoardDTO;
import com.ep.board.dto.PageRequestDTO;
import com.ep.board.dto.PageResultDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    void register() {

        BoardDTO dto = BoardDTO.builder()
                .title("Test..")
                .content("Test..")
                .writerEmail("user55@aaa.com") // 현재 DB에 존재하는 회원 이메일
                .build();

        Long bno = boardService.register(dto);

    }

    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);

        for(BoardDTO boardDTO : result.getDtoList()) {
            System.out.println(boardDTO);
        }
    }

    @Test
    public void testGet() {

        Long bno = 100L;

        BoardDTO boardDTO = boardService.get(bno);

        System.out.println(boardDTO);

    }

    @Test
    public void testRemove() {

        Long bno = 1L;

        boardService.removeWithReplies(bno);

    }

    @Test
    public void testModify(){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(2L)
                .title("제목을 변경합니다.")
                .content("내용 변경합니다.")
                .build();

        boardService.modify(boardDTO);
    }

}
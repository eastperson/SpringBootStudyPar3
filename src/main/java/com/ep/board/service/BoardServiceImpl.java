package com.ep.board.service;

import com.ep.board.dto.BoardDTO;
import com.ep.board.dto.PageRequestDTO;
import com.ep.board.dto.PageResultDTO;
import com.ep.board.entity.Board;
import com.ep.board.entity.Member;
import com.ep.board.repository.BoardRepository;
import com.ep.board.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.function.Function;

@RequiredArgsConstructor
@Log4j2
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO dto) {

        log.info(dto);

        Board board = dtoToEntity(dto);

        boardRepository.save(board);

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        Function<Object[],BoardDTO> fn = (en -> entityToDTO((Board) en[0], (Member)en[1], (Long)en[2]));

        //Page<Object[]> result = boardRepository.getBoardWithReplyCount(
        //        pageRequestDTO.getPageable(Sort.by("bno").descending()));
        Page<Object[]> result = boardRepository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending())
        );

        return new PageResultDTO<>(result,fn);
    }

    @Override
    public BoardDTO get(Long bno) {

        Object result = boardRepository.getBoardByBno(bno);

        Object[] arr = (Object[]) result;

        return entityToDTO((Board) arr[0],(Member) arr[1],(Long) arr[2]);
    }

    @Transactional
    @Override
    public void removeWithReplies(Long bno) {
        // 댓글부터 삭제
        replyRepository.deleteByBno(bno);

        boardRepository.deleteById(bno);
    }

    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {

        Board board = boardRepository.getOne(boardDTO.getBno());

        if(board != null) {

            board.changeTitle(boardDTO.getTitle());
            board.changeContent(boardDTO.getContent());

            boardRepository.save(board);

        }
    }
}

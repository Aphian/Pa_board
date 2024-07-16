package jpa.board.service;

import jpa.board.dto.BoardDto;
import jpa.board.entity.Board;
import jpa.board.entity.Member2;
import jpa.board.repository.BoardRepository;
import jpa.board.repository.Member2Repository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final Member2Repository member2Repository;
	
	public Board selectBoardDetail(Long id) {
		return boardRepository.findById(id).get();
	}
	
	@Transactional
	public Long saveBoard(BoardDto boardDto) {
		List<Member2> memberList = member2Repository.findAll();
		Member2 member = memberList.get(0);
		Board board = null;
		
		if(boardDto.getId() == null) {
			board = boardDto.toEntity(member);
			boardRepository.save(board);
		} else {
			board = boardRepository.findById(boardDto.getId()).get();
			board.update(boardDto.getTitle(), boardDto.getContent());
		}
		
		return board.getId();
		
	}
	
	@Transactional
	public Board deleteBoard(Long id) {
		
		Board board = boardRepository.findById(id).get();
		
		board.delete("Y");
		
		return board;
		
	}

}

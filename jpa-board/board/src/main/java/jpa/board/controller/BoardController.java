package jpa.board.controller;


import jpa.board.dto.BoardDto;
import jpa.board.entity.Board;
import jpa.board.repository.BoardRepository;
import jpa.board.repository.CustomBoardRepository;
import jpa.board.service.BoardService;
import jpa.board.service.FileService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;


@Controller
@RequiredArgsConstructor
public class BoardController {
	
	private final CustomBoardRepository customBoardRepository;
	private final BoardService boardService;
	
	private final FileService fileService;
	
	@GetMapping("/")
	public String list(@RequestParam(name = "searchVal", required = false) String searchVal, @PageableDefault(size = 10) Pageable pageable, Model model) {
		Page<BoardDto> results = customBoardRepository.seleteBoardList(searchVal, pageable);
		model.addAttribute("list", results);
		model.addAttribute("maxPage", 5);
		model.addAttribute("searchVal", searchVal);
		
		pageModelPut(results, model);
		
		return "board/list";
		
	}
	
	private void pageModelPut(Page<BoardDto> results, Model model) {
		model.addAttribute("totalCount", results.getTotalElements());
		model.addAttribute("size", results.getPageable().getPageSize());
		model.addAttribute("number", results.getPageable().getPageNumber());
	}
	
	
	@GetMapping("/write")
	public String write(Model model) {
		model.addAttribute("boardDto", new BoardDto());
		return "board/write";
	}
	
	@PostMapping("/write")
	public String save(@Valid BoardDto boardDto, BindingResult result) throws Exception {
		
		if (result.hasErrors()) {
			return "board/write";
		}
		
		boardService.saveBoard(boardDto);
		return "redirect:/";
	}
	
	
	@GetMapping("/updata/{boardId}")
	public String detail(@PathVariable(name = "boardId") Long boardId, Model model) {
		Board board = boardService.selectBoardDetail(boardId);
		
		BoardDto boardDto = BoardDto.builder()
							.id(boardId)
							.title(board.getTitle())
							.content(board.getContent())
							.build();

		model.addAttribute("boardDto", customBoardRepository.selectBoardFileDetail(boardId));
		model.addAttribute("boardDto",boardDto);
		
		return "board/update";
	}
	
	@PutMapping("/update/{boardId}")
	public String update(@Valid BoardDto boardDto, BindingResult result) throws Exception {
		
		if(result.hasErrors()) {
			return "board/update";
		}
		
		boardService.saveBoard(boardDto);
		return "redirect:/";
	}
	
	@PostMapping("/delete")
	public String delete(@RequestParam(name = "boarIds", required = false) List<String> boardIds) {
		
		for (int i = 0; i < boardIds.size(); i++) {
			Long id = Long.valueOf(boardIds.get(i));
			boardService.deleteBoard(id);
		}
		
		return "redirect:/";
	}
	
	@PostMapping("/boardFileDelete")
	public String boardFileDelete(@RequestParam(name = "fileId", required = false) Long fileId, @RequestParam(name = "boardId", required = false) Long boardId) {
		
		fileService.deleteBoardFile(fileId);
		
		return "redirect:/update"+ boardId;
		
	}

}

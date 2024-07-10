package jpa.board.controller;


import jpa.board.dto.BoardDto;
import jpa.board.repository.CustomBoardRepository;
import jpa.board.service.BoardService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;


@Controller
@RequiredArgsConstructor
public class BoardController {
	
	private final CustomBoardRepository customBoardRepository;
//	private final BoardService boardService;
	
	public String list(String searchVal, Pageable pageable, Model model) {
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
	
	
//	@GetMapping("/")
//	public String list() {
//		return "board/list";
//	}
	
	@GetMapping("/write")
	public String write() {
		return "board/write";
	}
	
	@GetMapping("/update")
	public String update() {
		return "board/update";
	}
	
//	@PostMapping("/delete")
//	public String delete(@RequestParam List<String> boardIds) {
//		
//		for (int i = 0; i < boardIds.size(); i++) {
//			Long id = Long.valueOf(boardIds.get(i));
//			boardService.deleteBoard(id);
//		}
//		
//		return "redirect:/";
//	}

}

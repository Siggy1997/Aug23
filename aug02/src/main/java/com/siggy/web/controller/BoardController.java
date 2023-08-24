package com.siggy.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siggy.web.dto.BoardDTO;
import com.siggy.web.service.BoardService;

@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	
	@GetMapping("/board")
	public String board(Model model) {
		List<BoardDTO> list = boardService.boardList();
		System.out.println(list);
		model.addAttribute("list", list);		
		return "board";
	}
	
	@ResponseBody
	@PostMapping("/detail")
	public String detail(@RequestParam("bno") int bno ) {
		//System.out.println(bno);
		BoardDTO dto = boardService.detail(bno);
		
		
		
		//읽음수 +1하기
		
		JSONObject json = new JSONObject();
		/*
		 * JSONObject e = new JSONObject(); e.put("content", dto.getBcontent());
		 * e.put("uuid", dto.getUuid());
		 */
		json.put("content", dto.getBcontent());
		json.put("uuid", dto.getUuid());
		json.put("ip", dto.getBip());
		
		
		System.out.println(json.toString());
		return json.toString();
	}

	@GetMapping("/write")
	public String write() {
		return "write";
	}
	
	@PostMapping("/write")
	public String write(HttpServletRequest request, HttpSession session) {
		//System.out.println(request.getParameter("title"));
		//System.out.println(request.getParameter("content"));
		if (session.getAttribute("mid") != null) {
			BoardDTO dto = new BoardDTO();
			dto.setBtitle(request.getParameter("title"));
			dto.setBcontent(request.getParameter("content"));
			
			dto.setM_id((String)session.getAttribute("mid"));
			int result = boardService.write(dto);
			
		}
		return "redirect:/board";
	}
	
	
	@PostMapping("/delete")
	public String delete(BoardDTO boardDTO) {
		System.out.println(boardDTO.getBno());
		System.out.println(boardDTO.getUuid());
		return "redirect:/board";
	}

	@ResponseBody
	@PostMapping("/detail2")
	public String detail2(@RequestParam("bno") int bno) throws JsonProcessingException {
		BoardDTO detail = boardService.detail2(bno);
			
		ObjectMapper mapp = new ObjectMapper();
		String json = mapp.writeValueAsString(detail);
		System.out.println(json);
		return json;
	}
	
}

package kr.or.connect.reservation.controller;

import kr.or.connect.reservation.anotation.AnoComment;
import kr.or.connect.reservation.anotation.AuthUser;
import kr.or.connect.reservation.dto.CommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import kr.or.connect.reservation.domain.Users;
import kr.or.connect.reservation.service.CommentService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {
	@Autowired
	private CommentService commentService;

	@GetMapping("/{id}")
	public ModelAndView loadReviewWritePage(@PathVariable("id") long id, Model model){
		String title = commentService.selectProductById(id);
		model.addAttribute("productId",id);
		model.addAttribute("title",title);
		ModelAndView mav= new ModelAndView("reviewWrite");
		return mav;
	}

	@PostMapping
	@ResponseBody
	public void registerReview(@AnoComment CommentDto commentDto,@AuthUser Users users, @RequestParam(name="file") MultipartFile[] files)  {
		commentDto.setFiles(files);
		commentDto.setUserId(users.getId());

		commentService.insertComment(commentDto);
	}
}

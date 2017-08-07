package kr.or.connect.reservation.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import kr.or.connect.reservation.config.NaverApiConfig;

@Controller
public class MainController {
	
	@Autowired
	private NaverApiConfig naverConfig;
	
	@GetMapping("/")
	public String index() {
		return "mainpage";
	}

	@GetMapping("/my")
	public String myreservation(Model model, HttpSession session) {
		return "myreservation";
	}

	@GetMapping("/products/{id}")
	public String detailProduct(Model model, @PathVariable("id") Long id) {
		//model.addAttribute("id", id);
		return "detail";
	}

	@GetMapping("/products/{id}/reserve")
	public String reserveProduct(HttpSession session, @PathVariable("id") Long id) {
		return "reserve";
	}


	@GetMapping("/products/{id}/reviews")
	public String review(Model model, @PathVariable("id") Long id) {
		model.addAttribute("id", id);
		return "review";
	}

	@GetMapping("/login")
	public String login(HttpSession session) {
		String accessToken = (String) session.getAttribute("token");
		String state = naverConfig.generateState(); // 토큰을 생성합니다.
		session.setAttribute("state", state); // 세션에 토큰을 저장합니다.


		// accessToken을 검증하여 로그인화면을 띄울 것인지 로그인 요청을 할 것인지에 대한 분개
		if(accessToken != null) {
			//System.out.println("forward...2");
			return "redirect:" + naverConfig.getOauthCallbackURL(state);
		} else {
			//System.out.println("forward...3");
			return "redirect:" + naverConfig.reAuthenticateUrl(state);
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) throws UnsupportedEncodingException {
		request.getSession().removeAttribute("loginedUser");
		request.getSession().removeAttribute("token");
		return "redirect:/";
	}

	@GetMapping("/reviewWrite/{id}")
	public String reviewWrite(@PathVariable("id") long id) {
		return "forward:/api/comments/"+id;
	}


}
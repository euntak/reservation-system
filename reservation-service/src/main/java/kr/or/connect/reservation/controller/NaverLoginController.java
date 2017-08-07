package kr.or.connect.reservation.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.connect.reservation.config.NaverApiConfig;
import kr.or.connect.reservation.domain.Users;
import kr.or.connect.reservation.dto.NaverUserDto;
import kr.or.connect.reservation.service.NaverLoginService;
import kr.or.connect.reservation.utils.Utils;

@Controller
@RequestMapping("/login")
public class NaverLoginController {

	final Logger logger = LoggerFactory.getLogger(NaverLoginController.class);

	@Autowired
	NaverApiConfig naverApiConfig;
	
	@Autowired
	NaverLoginService service;

	/**
	 * 네아로 콜백 매핑 URL
	 * @param state
	 * @param code
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/callback")
	public String callback(
			@RequestParam String state,
			@RequestParam String code,
			HttpServletRequest request)
			throws UnsupportedEncodingException {

		Map<String, String> responseUserToJson = null;
		NaverUserDto naverUser = null;

		// 세션에 저장된 토큰을 받아옵니다.
		String storedState = (String) request.getSession().getAttribute("state");
		String redirectUrl = (String) request.getSession().getAttribute("prevPage");
		
		// 세션에 저장된 토큰과 인증을 요청해서 받은 토큰이 일치하는지 검증합니다.
		if (!state.equals(storedState)) {
			//System.out.println("401 unauthorized");
			return "redirect:/";
		}
		
		// AccessToken을 요청하고 그 값을 가져옵니다.
		String data = Utils.getHtml(naverApiConfig.getAccessTokenUrl(state, code), null);

		// JSON의 형태로 받아온 값을 Map으로 저장합니다.
		Map<String, String> map = Utils.JSONStringToMap(data);
		String accessToken = map.get("access_token");
		String tokenType = map.get("token_type");
		
		if (accessToken != null && tokenType != null) {
			request.getSession().setAttribute("token", accessToken);
			if (accessToken != null && tokenType != null) {
				responseUserToJson = naverApiConfig.getLoginedUserInfo(accessToken, tokenType, "XML");
				naverUser = new NaverUserDto();
				naverUser.setEmail(responseUserToJson.get("email"));
				naverUser.setId(responseUserToJson.get("id"));
				naverUser.setNickname(responseUserToJson.get("nickname"));
				naverUser.setProfileImage(responseUserToJson.get("profile_image"));
				
			}
		}

		logger.debug("naverUser : {}", naverUser);

		// 해당 user정보가 DB에 있는지 검증, 및 업데이트
		Users loginedUser = service.login(naverUser);
		
		if(loginedUser != null) {
			request.getSession().setAttribute("loginedUser", loginedUser);
			if (redirectUrl != null) {
				return "redirect:" + redirectUrl;
			}
		}

		return "redirect:/";

	}
}

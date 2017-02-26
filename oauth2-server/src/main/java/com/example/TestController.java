package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("test")
class TestController {
    @RequestMapping("authorization-code")
	@ResponseBody
	public String authorizationCodeTest(@RequestParam("code") String code) {
        String scope = "";
        String clientId = "my_client_id";
        String clientSecret = "my_client_secret";
        String redirectUri = "http://localhost:8080/test/authorization-code";
		String curl = String.format("curl " +
				"-F \"grant_type=authorization_code\" " +
				"-F \"code=%s\" " +
				"-F \"scope=%s\" " +
				"-F \"client_id=%s\" " +
				"-F \"client_secret=%s\" " +
				"-F \"redirect_uri=%s\" " +
				"\"http://%s:%s@localhost:8080/oauth/token\"", code, scope, clientId, clientSecret, redirectUri, clientId, clientSecret);
		return curl;
	}
}
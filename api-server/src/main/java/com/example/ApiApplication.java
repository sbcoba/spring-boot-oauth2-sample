package com.example;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@EnableResourceServer
@SpringBootApplication
public class ApiApplication {

	@Bean
	public ResourceServerConfigurerAdapter resourceServerConfigurerAdapter() {
		return new ResourceServerConfigurerAdapter() {
			@Override
			public void configure(HttpSecurity http) throws Exception {
				http.headers().frameOptions().disable();
				http.authorizeRequests()
						.antMatchers("/members", "/members/**").access("#oauth2.hasScope('user.email')")
						.anyRequest().authenticated();
			}
		};
	}

	/**
	 * API를 조회시 출력될 테스트 데이터
	 * @param memberRepository
	 * @return
	 */
	@Bean
	public CommandLineRunner commandLineRunner(MemberRepository memberRepository) {
		return args -> {
			memberRepository.save(new Member("이철수", "chulsoo", "chulsoo@gmail.com", "01012345678", "철렁이", Calendar.getInstance().getTime(), "test111"));
			memberRepository.save(new Member("김정인", "jungin11", "jungin11@outlook.com", "01099822312", "인정이", Calendar.getInstance().getTime(), "test222"));
			memberRepository.save(new Member("류정우", "jwryu991", "jwryu991@yahoo.com", "01031213333", "우정이", Calendar.getInstance().getTime(), "test333"));
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}

@RestController
class MemberContoller {
    private static final Logger log = LoggerFactory.getLogger(MemberContoller.class);

    @Autowired
    MemberRepository memberRepository;

    @PreAuthorize("#oauth2.hasScope('member.info.public')")
    @RequestMapping("/api/member")
    public MemberData member(@AuthenticationPrincipal OAuth2Authentication authentication) {
        String username = authentication.getUserAuthentication().getPrincipal().toString();
        Set<String> scopes = authentication.getOAuth2Request().getScope();
        log.info("Member's username = {}", username);
        log.info("Client scope info = {}", scopes);
        Member member = memberRepository.findByUsername(username);
        return MemberData.from(member,scopes);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MemberData {
        Long id;
        String name;
        String username;
        String email;
        String phone;
        String nick;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Date lastLoginDate;

        public static MemberData from(Member member, Set<String> scopes) {
            MemberData memberData = new MemberData();
            memberData.id = member.getId();
            memberData.name = member.getName();
            memberData.username = member.getName();
            if (scopes.contains("member.info.email")) {
                memberData.email = member.getEmail();
            }
            if (scopes.contains("member.info.phone")) {
                memberData.phone = member.getPhone();
            }
            if (scopes.contains("member.info.nick")) {
                memberData.nick = member.getNick();
            }
            if (scopes.contains("member.info.last_login_date")) {
                memberData.lastLoginDate = member.getLastLoginDate();
            }
            return memberData;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getNick() {
            return nick;
        }

        public Date getLastLoginDate() {
            return lastLoginDate;
        }
    }
}
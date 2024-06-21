package com.example.demo.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.entity.Member;
import com.example.demo.model.vo.ResponseVO;
import com.example.demo.service.MemberService;

import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberApiController {
	
	private final MemberService memberService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	/**
	 * 회원 가입
	 * @param 아이디, 이름, 닉네임, 비밀번호
	 * @return
	 */
    @PostMapping(value="/join", produces="application/json")
    public ResponseEntity<ResponseVO> saveMember(@RequestBody Member member) {
    	try {
    		Member newMember  = memberService.join(member);
        	return  ResponseEntity.ok(
    				ResponseVO.builder()
    				.statusCode(200)
    				.message("회원가입이 되었습니다.")
    				.data(newMember)
    				.build()
    		);
    	} catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    	            .body(
    	                ResponseVO.builder()
    	                    .statusCode(500)
    	                    .message("내부 문제로 회원가입이 실패했습니다.")
    	                    .build()
    	            );
    	}
    	

    }
    
    /**
	 * 로그인
	 * @param id, password
	 * @return
	 */
    @PostMapping(value="/login", produces="application/json")
    public ResponseEntity<ResponseVO> getMemberId(HttpServletRequest request, @RequestBody Member member) {	
		try {
			// 세션 처리
			if(member != null) {
				HttpSession session = request.getSession();
	            session.setAttribute("loginMember", member);
	            session.setMaxInactiveInterval(60 * 30);
			}
			
			// 정보 확인
			boolean isAuthenticated = memberService.login(member);
			
			if(isAuthenticated) {
//				saveContext(member.getId());
				// 세션 등록
//				Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(member.getId(), member.getPassword()));
//				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				
	            return ResponseEntity.ok(
	                ResponseVO.builder()
	                    .statusCode(200)
	                    .message("로그인이 완료되었습니다.")
	                    .data(member)
	                    .build()
	            );
	    	} else {
	    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	    	            .body(
	    	                ResponseVO.builder()
	    	                    .statusCode(401)
	    	                    .message("잘못된 비밀번호입니다.")
	    	                    .build()
	    	            );
	    	}
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	    	            .body(
	    	                ResponseVO.builder()
	    	                    .statusCode(500)
	    	                    .message("해당 아이디가 없습니다.")
	    	                    .build()
	    	            );
		}
    	
    }
    

    
    @DeleteMapping(value="/{id}", produces="application/json")
    public ResponseEntity<ResponseVO> deleteMember(@PathVariable(name="id") String id) {	
    	try {
    		boolean isDeleted = memberService.deleteMember(id);
    		
        	if(isDeleted) {
        		 return ResponseEntity.ok(
     	                ResponseVO.builder()
     	                    .statusCode(200)
     	                    .message("계정이 탈퇴되었습니다.") 	                 
     	                    .build()
     	         );
        	} 
    	} catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    	            .body(
    	                ResponseVO.builder()
    	                    .statusCode(500)
    	                    .message("해당 계정이 없습니다.")
    	                    .build()
    	     );
    	}
    	return ResponseEntity.status(HttpStatus.NOT_FOUND)
	            .body(
	                ResponseVO.builder()
	                    .statusCode(404)
	                    .message("해당 계정이 없습니다.")
	                    .build()
	     );
    	
    }
}

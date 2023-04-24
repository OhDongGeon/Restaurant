package com.example.restaurant.security.filter;

import com.example.restaurant.Service.CommonService;
import com.example.restaurant.exception.FindException;
import com.example.restaurant.domain.vo.MemberVo;
import com.example.restaurant.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.restaurant.exception.ErrorCode.TOKEN_ERROR;


@RequiredArgsConstructor
@WebFilter(urlPatterns = {"/user/*", "/manager/*"})
public class TokenFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final CommonService tokenCheckService;

    public final String TOKEN_HEADER = "MEMBER_TOKEN";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader(TOKEN_HEADER);


        if (!StringUtils.hasText(token) || !this.tokenProvider.validateToken(token)) {
            throw new FindException(TOKEN_ERROR);
        }

        MemberVo memberVo = tokenProvider.getMemberVo(token);
        tokenCheckService.findByIdAndEmail(memberVo.getMemberId(), memberVo.getMemberEmail())
                .orElseThrow(() -> new FindException(TOKEN_ERROR));

        filterChain.doFilter(request, response);
    }
}

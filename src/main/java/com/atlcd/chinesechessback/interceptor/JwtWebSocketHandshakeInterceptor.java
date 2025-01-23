package com.atlcd.chinesechessback.interceptor;

import com.atlcd.chinesechessback.common.auth.UserHolder;
import com.atlcd.chinesechessback.common.constants.JwtClaimsConstant;
import com.atlcd.chinesechessback.common.exception.BusinessException;
import com.atlcd.chinesechessback.common.exception.enumeration.UserCodeEnum;
import com.atlcd.chinesechessback.common.properties.JwtProperties;
import com.atlcd.chinesechessback.common.utils.JwtUtil;
import com.atlcd.chinesechessback.mapper.UserMapper;
import com.atlcd.chinesechessback.pojo.entity.User;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtWebSocketHandshakeInterceptor implements HandshakeInterceptor {
    private final RedisTemplate redisTemplate;
    private final JwtProperties jwtProperties;
    private final UserMapper userMapper;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 从请求头中获取 token
        ServletServerHttpRequest req = (ServletServerHttpRequest) request;
        //获取token认证
        String token = req.getServletRequest().getParameter(jwtProperties.getUserTokenName());
        try {
            // 校验令牌
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Integer userId = Integer.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            User user = userMapper.selectById(userId);

            if(user == null || !token.equals(redisTemplate.opsForValue().get("user:" + user.getId()))) {
                return false;
            }
            attributes.put("User", user);
            // 将用户信息设置到 UserHolder
            return true;
        } catch (Exception ex) {
            BusinessException.fail(UserCodeEnum.NOT_LOGIN_ERROR);
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
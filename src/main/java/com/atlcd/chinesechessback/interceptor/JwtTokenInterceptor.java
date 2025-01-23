package com.atlcd.chinesechessback.interceptor;


import com.atlcd.chinesechessback.common.auth.UserHolder;
import com.atlcd.chinesechessback.common.constants.JwtClaimsConstant;
import com.atlcd.chinesechessback.common.exception.BusinessException;
import com.atlcd.chinesechessback.common.exception.enumeration.UserCodeEnum;
import com.atlcd.chinesechessback.common.properties.JwtProperties;
import com.atlcd.chinesechessback.common.utils.JwtUtil;
import com.atlcd.chinesechessback.pojo.entity.User;
import com.atlcd.chinesechessback.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {
    private final RedisTemplate redisTemplate;

    private final JwtProperties jwtProperties;

    private final UserMapper userMapper;
    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());

        //2、校验令牌
        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Integer userId = Integer.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            User user = userMapper.selectById(userId);
            if(user == null || !token.equals(redisTemplate.opsForValue().get("user:" + user.getId())) ) {
                throw new Exception();
            }
            UserHolder.setCurrentIUser(user);
            return true;
        } catch (Exception ex) {
            BusinessException.fail(UserCodeEnum.NOT_LOGIN_ERROR);
            return false;
        }
    }
}
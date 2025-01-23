package com.atlcd.chinesechessback.controller;


import com.atlcd.chinesechessback.common.constants.JwtClaimsConstant;
import com.atlcd.chinesechessback.common.exception.BusinessException;
import com.atlcd.chinesechessback.common.exception.enumeration.UserCodeEnum;
import com.atlcd.chinesechessback.common.properties.JwtProperties;
import com.atlcd.chinesechessback.common.result.Result;
import com.atlcd.chinesechessback.common.utils.BeanUtil;
import com.atlcd.chinesechessback.common.utils.JwtUtil;
import com.atlcd.chinesechessback.pojo.dto.UserLoginDto;
import com.atlcd.chinesechessback.pojo.dto.UserRegisterDto;
import com.atlcd.chinesechessback.pojo.entity.User;
import com.atlcd.chinesechessback.pojo.vo.UserLoginVo;
import com.atlcd.chinesechessback.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Tag(name = "用户相关接口接口")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final RedisTemplate redisTemplate;

    private final UserService userService;

    private final JwtProperties jwtProperties;

    @PostMapping("/login")
    public Result<UserLoginVo> login(@RequestBody UserLoginDto userLoginDto){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userLoginDto.getUsername())
                .eq(User::getPasswordMd5, userLoginDto.getPasswordMd5());
        User user = userService.getOne(wrapper);
        if(user == null){
            BusinessException.fail(UserCodeEnum.USER_LOGIN_ERROR);
        }
        log.info("用户登录：{}", userLoginDto);
        // 登录成功后，生成jwt令牌
        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);
        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setUsername(user.getUsername());
        userLoginVo.setToken(token);
        redisTemplate.opsForValue().set("user:" + user.getId(), token);
        return Result.ok(userLoginVo);
    }

    @PostMapping("/register")
    public Result<Void> login(@RequestBody UserRegisterDto userRegisterDto){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userRegisterDto.getUsername());
        User user = userService.getOne(wrapper);
        if(user != null){
            BusinessException.fail(UserCodeEnum.USER_EXIST_ERROR);
        }
        log.info("用户注册：{}", userRegisterDto);
        user = new User();
        BeanUtil.copyProperties(userRegisterDto, user);
        userService.save(user);
        return Result.ok();
    }

    @Operation(summary = "检查用户登录状态")
    @GetMapping("/auth")
    public Result<Void> checkLogin() {
        return Result.ok();
    }
}

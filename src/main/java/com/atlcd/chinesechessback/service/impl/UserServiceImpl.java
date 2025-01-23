package com.atlcd.chinesechessback.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atlcd.chinesechessback.pojo.entity.User;
import com.atlcd.chinesechessback.service.UserService;
import com.atlcd.chinesechessback.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 12548
* @description 针对表【user(用户账号)】的数据库操作Service实现
* @createDate 2024-10-09 22:39:08
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}





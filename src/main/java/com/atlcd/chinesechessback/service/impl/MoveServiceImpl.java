package com.atlcd.chinesechessback.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atlcd.chinesechessback.pojo.entity.Move;
import com.atlcd.chinesechessback.service.MoveService;
import com.atlcd.chinesechessback.mapper.MoveMapper;
import org.springframework.stereotype.Service;

/**
* @author 12548
* @description 针对表【move(对局移动)】的数据库操作Service实现
* @createDate 2024-10-31 11:06:56
*/
@Service
public class MoveServiceImpl extends ServiceImpl<MoveMapper, Move>
    implements MoveService{

}





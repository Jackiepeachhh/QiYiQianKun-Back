package com.atlcd.chinesechessback.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atlcd.chinesechessback.pojo.entity.Game;
import com.atlcd.chinesechessback.service.GameService;
import com.atlcd.chinesechessback.mapper.GameMapper;
import org.springframework.stereotype.Service;

/**
* @author 12548
* @description 针对表【game(对局)】的数据库操作Service实现
* @createDate 2024-10-31 20:12:15
*/
@Service
public class GameServiceImpl extends ServiceImpl<GameMapper, Game>
    implements GameService{

}





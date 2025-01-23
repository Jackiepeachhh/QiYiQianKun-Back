package com.atlcd.chinesechessback.controller;


import com.atlcd.chinesechessback.common.auth.UserHolder;
import com.atlcd.chinesechessback.common.result.Result;
import com.atlcd.chinesechessback.common.utils.BeanUtil;
import com.atlcd.chinesechessback.pojo.entity.Game;
import com.atlcd.chinesechessback.pojo.entity.Move;
import com.atlcd.chinesechessback.pojo.entity.User;
import com.atlcd.chinesechessback.pojo.game.Piece;
import com.atlcd.chinesechessback.pojo.game.Position;
import com.atlcd.chinesechessback.pojo.vo.GameInfoVo;
import com.atlcd.chinesechessback.pojo.vo.MoveVo;
import com.atlcd.chinesechessback.service.GameService;
import com.atlcd.chinesechessback.service.MoveService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/game")
@Tag(name = "对局相关接口接口")
@Slf4j
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    private final MoveService moveService;

    @PostMapping("/list")
    public Result<List<GameInfoVo>> list(){
        User user = UserHolder.getCurrentUser();
        String username = user.getUsername();
        LambdaQueryWrapper<Game>  gameWrapper = new LambdaQueryWrapper<>();
        gameWrapper.eq(Game::getRed,username).or().eq(Game::getBlack,username);
        List<Game> gameList= gameService.list(gameWrapper);
        List<GameInfoVo> gameInfoVoList = BeanUtil.copyList(gameList, GameInfoVo.class);
        gameInfoVoList.forEach(gameInfoVo -> {
            gameInfoVo.setIsRed(username.equals(gameInfoVo.getRed()));
            LambdaQueryWrapper<Move> moveWrapper = new LambdaQueryWrapper<>();
            moveWrapper.eq(Move::getGameId,gameInfoVo.getId());
            int stepNum = (int) moveService.count(moveWrapper);
            gameInfoVo.setStepNum(stepNum);
        });
        return Result.ok(gameInfoVoList);
    }

    @GetMapping("/moves/{gameId}")
    public Result<List<MoveVo>> getMoves(@PathVariable Integer gameId){
        LambdaQueryWrapper<Move> moveWrapper = new LambdaQueryWrapper<>();
        moveWrapper.eq(Move::getGameId,gameId).orderByAsc(Move::getStep);
        List<Move> moveList= moveService.list(moveWrapper);
        List<MoveVo> moveVoList=new ArrayList<>();
        moveList.forEach(move -> {
            MoveVo moveVo = new MoveVo();
            Position from =  new Position();
            from.setX(move.getFromX());
            from.setY(move.getFromY());
            moveVo.setFrom(from);
            Position to =  new Position();
            to.setX(move.getToX());
            to.setY(move.getToY());
            moveVo.setTo(to);
            moveVo.setMovePiece(new Piece(move.getMovePiece()));
            if(move.getEatPiece()!=null){
                moveVo.setEatPiece(new Piece(move.getEatPiece()));
            }
            moveVo.setMessage(move.getMessage());
            moveVoList.add(moveVo);
        });
        return Result.ok(moveVoList);
    }

}

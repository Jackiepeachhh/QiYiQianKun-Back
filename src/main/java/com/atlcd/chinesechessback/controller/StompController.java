package com.atlcd.chinesechessback.controller;



import com.atlcd.chinesechessback.common.constants.RequestConstant;
import com.atlcd.chinesechessback.common.utils.BeanUtil;
import com.atlcd.chinesechessback.pojo.entity.Game;
import com.atlcd.chinesechessback.pojo.entity.Move;
import com.atlcd.chinesechessback.pojo.entity.User;
import com.atlcd.chinesechessback.pojo.message.*;
import com.atlcd.chinesechessback.pojo.room.PlayingGame;
import com.atlcd.chinesechessback.pojo.room.Room;
import com.atlcd.chinesechessback.pojo.room.UserState;
import com.atlcd.chinesechessback.service.GameService;
import com.atlcd.chinesechessback.service.MoveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StompController {
    private final SimpMessagingTemplate messagingTemplate;

    private final RedisTemplate redisTemplate;

    private final GameService gameService;

    private final MoveService moveService;

    @MessageMapping("/{roomId}/move")
    @SendTo("/room/{roomId}/move") // 将返回的对象自动发送到指定的主题
    public MoveMessage handleMove(@Payload MoveMessage moveMessage, @DestinationVariable Integer roomId) {
        log.info("走子{}",moveMessage);
        Room room = (Room) redisTemplate.opsForValue().get("room:"+roomId);
        PlayingGame playingGame = room.getPlayingGame();

        // 得到新步
        Move move = new Move();
        move.setFromX(moveMessage.getFrom().getX());
        move.setFromY(moveMessage.getFrom().getY());
        move.setToX(moveMessage.getTo().getX());
        move.setToY(moveMessage.getTo().getY());
        move.setMovePiece(moveMessage.getMovePiece().getName());
        if(moveMessage.getEatPiece()!=null) {
            move.setEatPiece(moveMessage.getEatPiece().getName());
        }
        log.info(move.getMessage());
        playingGame.getMoves().add(move);
        playingGame.setIsRedTurn(!playingGame.getIsRedTurn());

        playingGame.move(moveMessage.getFrom(), moveMessage.getTo());

        redisTemplate.opsForValue().set("room:"+roomId, room);
        return moveMessage;
    }

    @MessageMapping("/{roomId}/join")
    @SendTo("/room/{roomId}/join") // 将返回的对象自动发送到指定的主题
    public JoinMessage handleJoin(SimpMessageHeaderAccessor headerAccessor,@DestinationVariable Integer roomId) {
        Room room = (Room) redisTemplate.opsForValue().get("room:"+roomId);
        User user = (User) headerAccessor.getSessionAttributes().get("User");
        UserState userState = new UserState();
        BeanUtil.copyProperties(user, userState);
        userState.setReady(false);
        JoinMessage joinMessage = new JoinMessage();
        if(room.getRedPlayer()==null && (room.getBlackPlayer()==null || !room.getBlackPlayer().getId().equals(user.getId()))) {
            room.setRedPlayer(userState);
            joinMessage.setIsRed(true);
            joinMessage.setPlayer(userState);
        }else if(room.getBlackPlayer()==null && (!room.getRedPlayer().getId().equals(user.getId()))){
            room.setBlackPlayer(userState);
            joinMessage.setIsRed(false);
            joinMessage.setPlayer(userState);
        }
        redisTemplate.opsForValue().set("room:"+roomId, room);
        return joinMessage;
    }

    @MessageMapping("/{roomId}/leave")
    @SendTo("/room/{roomId}/leave") // 将返回的对象自动发送到指定的主题
    public JoinMessage handleLeave(SimpMessageHeaderAccessor headerAccessor,@DestinationVariable Integer roomId) {
        Room room = (Room) redisTemplate.opsForValue().get("room:"+roomId);
        User user = (User) headerAccessor.getSessionAttributes().get("User");
        if(room.getIsPlaying()){
            GameOverMessage gameOverMessage = new GameOverMessage();
            String red = room.getRedPlayer().getUsername();
            String black = room.getBlackPlayer().getUsername();
            String username = user.getUsername();
            boolean isRed = red.equals(username);
            gameOverMessage.setLoser(isRed ? red : black);
            gameOverMessage.setWinner(isRed ? black : red);
            gameOverMessage.setReason(isRed ? "红方认输" : "黑方认输");
            gameOverMessage.setResult(isRed ? 0 : 1);
            gameOverNotification(gameOverMessage,roomId);
        }
        JoinMessage leaveMessage = new JoinMessage();
        if(room.getRedPlayer()!=null && room.getRedPlayer().getId().equals(user.getId())) {
            leaveMessage.setPlayer(room.getRedPlayer());
            leaveMessage.setIsRed(true);
            room.setRedPlayer(null);
        }else if(room.getBlackPlayer()!=null && room.getBlackPlayer().getId().equals(user.getId())) {
            leaveMessage.setPlayer(room.getBlackPlayer());
            leaveMessage.setIsRed(false);
            room.setBlackPlayer(null);
        }
        redisTemplate.opsForValue().set("room:"+roomId, room);
        return leaveMessage;
    }


    @MessageMapping("/{roomId}/ready")
    @SendTo("/room/{roomId}/ready") // 将返回的对象自动发送到指定的主题
    public ReadyMessage handleReady(SimpMessageHeaderAccessor headerAccessor,@DestinationVariable Integer roomId) {
        System.out.println("roomId ready");
        Room room = (Room) redisTemplate.opsForValue().get("room:"+roomId);
        User user = (User) headerAccessor.getSessionAttributes().get("User");
        ReadyMessage readyMessage = new ReadyMessage();
        Boolean isGameStart = false;
        if(room.getRedPlayer()!=null && room.getRedPlayer().getId().equals(user.getId())) {
            room.getRedPlayer().setReady(true);
            readyMessage.setIsRed(true);
            if(room.getBlackPlayer()!=null && room.getBlackPlayer().getReady()){
                isGameStart =true;
            }
        }else if(room.getBlackPlayer()!=null && room.getBlackPlayer().getId().equals(user.getId())) {
            room.getBlackPlayer().setReady(true);
            readyMessage.setIsRed(false);
            if(room.getRedPlayer()!=null && room.getRedPlayer().getReady()){
                isGameStart =true;
            }
        }
        redisTemplate.opsForValue().set("room:"+roomId, room);
        if(isGameStart){
            gameStartNotification(roomId);
        }
        return readyMessage;
    }

    @MessageMapping("/{roomId}/cancle-ready")
    @SendTo("/room/{roomId}/candle-ready") // 将返回的对象自动发送到指定的主题
    public ReadyMessage handleCancleReady(SimpMessageHeaderAccessor headerAccessor,@DestinationVariable Integer roomId) {
        Room room = (Room) redisTemplate.opsForValue().get("room:"+roomId);
        User user = (User) headerAccessor.getSessionAttributes().get("User");
        Integer userId = user.getId();
        ReadyMessage readyMessage = new ReadyMessage();
        if(room.getRedPlayer()!=null && room.getRedPlayer().getId().equals(userId)) {
            room.getRedPlayer().setReady(false);
            readyMessage.setIsRed(true);
        }else if(room.getBlackPlayer()!=null && room.getBlackPlayer().getId().equals(userId)) {
            room.getBlackPlayer().setReady(false);
            readyMessage.setIsRed(false);
        }
        redisTemplate.opsForValue().set("room:"+roomId, room);
        return readyMessage;
    }

    public void gameStartNotification(Integer roomId) {
        log.info("对局开始！");
        Room room = (Room) redisTemplate.opsForValue().get("room:"+roomId);
        room.setIsPlaying(true);
        PlayingGame playingGame =  PlayingGame.builder()
                .isGameOver(false)
                .isRedTurn(true)
                .moves(new ArrayList<>())
                .isGeneralChecking(false)
                .build();

        //创建棋盘
        playingGame.createInitBoard(PlayingGame.initBoard);

        //保存房间
        room.setPlayingGame(playingGame);
        redisTemplate.opsForValue().set("room:"+roomId, room);
        GameStartMessage gameStartMessage = new GameStartMessage();
        messagingTemplate.convertAndSend("/room/"+roomId+"/game-start",gameStartMessage);
    }

    @MessageMapping("/{roomId}/game-over")
    public void gameOverNotification(@Payload GameOverMessage gameOverMessage ,@DestinationVariable Integer roomId){
        Room room = (Room) redisTemplate.opsForValue().get("room:"+roomId);

        // 保存对局
        Game game = new Game();
        BeanUtil.copyProperties(gameOverMessage, game);
        game.setRed(room.getRedPlayer().getUsername());
        game.setBlack(room.getBlackPlayer().getUsername());
        gameService.save(game);

        List<Move> moves = room.getPlayingGame().getMoves();

        for(int i=0;i<moves.size();i++){
            Move move = moves.get(i);
            move.setGameId(game.getId());
            move.setStep(i+1);
        }
        moveService.saveBatch(moves);

        room.setIsPlaying(false);
        if(room.getRedPlayer()!=null) {
            room.getRedPlayer().setReady(false);
        }
        if(room.getBlackPlayer()!=null) {
            room.getBlackPlayer().setReady(false);
        }
        room.setPlayingGame(null);
        redisTemplate.opsForValue().set("room:"+roomId, room);
        messagingTemplate.convertAndSend("/room/"+roomId+"/game-over",gameOverMessage);
    }

    @MessageMapping("/{roomId}/request")
    @SendTo("/room/{roomId}/request")
    public RequestMessage gameOverNotification(@Payload RequestMessage requestMessage ,@DestinationVariable Integer roomId){
        log.info("房间{}对战一方发出请求{}",roomId,requestMessage);
        return requestMessage;
    }

    @MessageMapping("/{roomId}/response")
    @SendTo("/room/{roomId}/response")
    public ResponseMessage gameOverNotification(@Payload ResponseMessage responseMessage ,@DestinationVariable Integer roomId){
        log.info("房间{}对战一方回复请求{}",roomId,responseMessage);
        if(responseMessage.getResult()){
            switch (responseMessage.getType()){
                case RequestConstant.PEACE:
                    GameOverMessage gameOverMessage = new GameOverMessage();
                    gameOverMessage.setResult(-1);
                    gameOverMessage.setReason("双方和棋");
                    gameOverNotification(gameOverMessage,roomId);
                    break;
                case RequestConstant.UNMOVE:
                    unMove(roomId);
                    break;
            }
        }
        return responseMessage;
    }


    public void unMove(Integer roomId){

        Room room = (Room) redisTemplate.opsForValue().get("room:"+roomId);
        PlayingGame playingGame = room.getPlayingGame();
        List<Move> moves = playingGame.getMoves();
        int lastIndex = moves.size() - 1;
        Move move = moves.get(lastIndex);
        moves.remove(lastIndex);
        playingGame.unmove(move);
        redisTemplate.opsForValue().set("room:"+roomId, room);
        MoveMessage moveMessage = new MoveMessage();
        messagingTemplate.convertAndSend("/room/"+roomId+"/un-move",moveMessage);
    }

    @MessageMapping("/{roomId}/view")
    public void handleView(SimpMessageHeaderAccessor headerAccessor,@Payload ViewMessage viewMessage, @DestinationVariable Integer roomId){
        Room room = (Room) redisTemplate.opsForValue().get("room:"+roomId);
        User user = (User) headerAccessor.getSessionAttributes().get("User");
        if(viewMessage.getIsJoin()){
            room.getViewers().add(user.getUsername());
        }else{
            room.getViewers().remove(user.getUsername());
        }
        redisTemplate.opsForValue().set("room:"+roomId, room);
    }

}

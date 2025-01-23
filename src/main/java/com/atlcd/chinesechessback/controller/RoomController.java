package com.atlcd.chinesechessback.controller;


import com.atlcd.chinesechessback.common.auth.UserHolder;
import com.atlcd.chinesechessback.common.result.Result;
import com.atlcd.chinesechessback.common.utils.BeanUtil;
import com.atlcd.chinesechessback.pojo.room.Room;
import com.atlcd.chinesechessback.pojo.room.UserState;
import com.atlcd.chinesechessback.pojo.vo.RoomViewVo;
import com.atlcd.chinesechessback.pojo.vo.RoomVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/room")
@Tag(name = "对局相关接口接口")
@Slf4j
@RequiredArgsConstructor
public class RoomController {

    private final RedisTemplate redisTemplate;

    private final AtomicInteger atomicId = new AtomicInteger();

    @PostMapping("/join")
    public Result<Integer> join() {
        Set<String> keys = redisTemplate.keys("room:*");
        Integer roomId = null;
        for (String key : keys) {
            Room room = (Room) redisTemplate.opsForValue().get(key);
            if (room != null) {
                if(room.getRedPlayer()==null || room.getBlackPlayer()==null){
                    roomId = room.getRoomId();
                }
            }
        }
        if (roomId == null) {
            roomId = atomicId.incrementAndGet();
            Room room = new Room();
            room.setRoomId(roomId);
            room.setIsPlaying(false);
            room.setViewers(new ArrayList<>());
            redisTemplate.opsForValue().set("room:" + roomId, room);
        }
        return Result.ok(roomId);
    }

    @GetMapping("/{roomId}")
    public Result<RoomVo> get(@PathVariable Integer roomId) {
        Room room = (Room) redisTemplate.opsForValue().get("room:" + roomId);
        RoomVo roomVo = new RoomVo();
        BeanUtil.copyProperties(room, roomVo);
        return Result.ok(roomVo);
    }

    @PostMapping("/view/list")
    public Result<List<RoomViewVo>> listView() {
        Set<String> keys = redisTemplate.keys("room:*");
        List<RoomViewVo> roomViewVos = new ArrayList<>();
        for (String key : keys) {
            Room room = (Room) redisTemplate.opsForValue().get(key);
            if (room != null && room.getIsPlaying()) {
                RoomViewVo roomViewVo = new RoomViewVo();
                BeanUtil.copyProperties(room, roomViewVo);
                roomViewVo.setViewNum(room.getViewers().size());
                roomViewVo.setStepNum(room.getPlayingGame().getMoves().size());
                roomViewVos.add(roomViewVo);
            }
        }
        return Result.ok(roomViewVos);
    }

}

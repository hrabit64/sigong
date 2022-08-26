package com.hrabit64.sigong.domain.user;

import io.swagger.models.auth.In;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@RedisHash("user")
public class User {

    @Id
    private String ip;
    private Integer fileCnt;
    private Boolean isBan;

    @TimeToLive(unit = TimeUnit.MINUTES)
    private Long ttl;

    public void addFileCnt(Integer amount){
        fileCnt += amount;
    }

    public boolean equalsExcludeTTL(User user){
        return user.getIp().equals(ip) &&
                user.getFileCnt().equals(fileCnt) &&
                user.getIsBan().equals(isBan);
    }
}

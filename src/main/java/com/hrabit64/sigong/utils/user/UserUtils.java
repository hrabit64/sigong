package com.hrabit64.sigong.utils.user;

import com.hrabit64.sigong.dto.user.serviceDto.UserInfoResponseDto;
import com.hrabit64.sigong.exception.ErrorCode;
import com.hrabit64.sigong.exception.SigongException;
import com.hrabit64.sigong.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.hrabit64.sigong.config.SigongConfig.MaxFileCnt;
import static com.hrabit64.sigong.utils.user.UserStatusKey.*;

@RequiredArgsConstructor
@Component
public class UserUtils {

    private final UserServiceImpl userService;

    public UserStatusKey check(String ip){
        UserInfoResponseDto userInfoResponseDto = userService.findByIP(ip);

        if(userInfoResponseDto.getIp() == null) return OK;

        if(userInfoResponseDto.getIsBan()) return BAN;

        if(userInfoResponseDto.getFileCnt().equals(MaxFileCnt)) return LIMIT_CNT;

        return OK;
    }

    public void checkValid(String ip){
        UserInfoResponseDto userInfoResponseDto = userService.findByIP(ip);

        UserStatusKey statusKey = this.check(ip);

        switch (statusKey) {
            case LIMIT_CNT:
                throw new SigongException(ErrorCode.OVER_LIMIT_CNT);

            case BAN:
                throw new SigongException(ErrorCode.YOUR_BAN);
        }
    }
}

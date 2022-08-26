package com.hrabit64.sigong.dto.user.serviceDto;


import com.hrabit64.sigong.domain.user.User;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoResponseDto {

    private String ip;
    private Integer fileCnt;
    private Boolean isBan;

    public UserInfoResponseDto(User user){
        ip = user.getIp();
        fileCnt = user.getFileCnt();
        isBan = user.getIsBan();
    }
    @Override
    public boolean equals(Object o){
        return this.toString().equals(o.toString());
    }
}

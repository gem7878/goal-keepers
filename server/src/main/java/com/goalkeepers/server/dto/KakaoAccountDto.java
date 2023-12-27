package com.goalkeepers.server.dto;

import lombok.Getter;

@Getter
public class KakaoAccountDto {
    
    private Long id;
    private KaKaoAccount kakao_account;

    @Getter
    public class KaKaoAccount {
        private String email;
        private Profile profile;

        @Getter
        public class Profile {
            private String nickname;
        }
    }
}

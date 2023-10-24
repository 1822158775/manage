package com.example.manage.util.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @avthor 潘小章
 * @date 2023/10/12
 */
@Data
@ToString
public class TokenPersonnel {
    public String personnelId;
    public String token_code;

    public TokenPersonnel() {
    }

    public TokenPersonnel(String personnelId, String token_code) {
        this.personnelId = personnelId;
        this.token_code = token_code;
    }
}

package models;

import lombok.Data;

@Data
public class UserLoginModel {
    private String userName,
            password;
}

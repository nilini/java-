package com.pgy.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Setter
@Getter
public class User {
    private long id;
    private String email;
    private String username;
    private String password;
    private String role;
    private int status;
    private Date regTime;
    private String regIp;
}

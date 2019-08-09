package com.panshi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author huangxiaolei
 */
@Getter
@Setter
@ToString
public class QuaysMember implements Serializable {

  private static final long serialVersionUID = 789456123L;

  private long id;
  private String username;
  private String password;
  private long userIntegral;
  private List<QuaysIntegral> quaysIntegral;

  public QuaysMember(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public QuaysMember() {
  }
}

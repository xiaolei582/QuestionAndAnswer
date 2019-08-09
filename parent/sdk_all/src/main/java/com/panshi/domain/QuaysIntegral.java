package com.panshi.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author huangxiaolei
 */
@Getter
@Setter
@ToString
public class QuaysIntegral implements Serializable {

  private static final long serialVersionUID = 741852963L;

  private long integralId;
  private long mid;
  private long integral;
  private String time;
  private String state;

  public QuaysIntegral() {
  }

  public QuaysIntegral(long mid, long integral, String state) {
    this.mid = mid;
    this.integral = integral;
    this.state = state;
  }
}

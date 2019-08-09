package com.panshi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
* @description 问题表属性
* @author huangxiaolei
* @create 2019-07-19
*/
@Getter
@Setter
@ToString
public class QuaysQuestion implements Serializable {

  private static final long serialVersionUID = 753851L;

  private long questionId;
  private long mid;
  private String question;
  private long integral;
  private String time;
  private QuaysMember quaysMember;
  private List<QuaysAnswer> quaysAnswerList;

  public QuaysQuestion(long mid, long integral) {
    this.mid = mid;
    this.integral = integral;
  }

  public QuaysQuestion() {
  }
}

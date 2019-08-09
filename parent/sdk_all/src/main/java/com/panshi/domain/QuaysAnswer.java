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
public class QuaysAnswer implements Serializable {

  private static final long serialVersionUID = 123456789L;

  private long id;
  private long mid;
  private long qid;
  private String answer;
  private String time;
  private String type;

  public QuaysAnswer() {
  }

  public QuaysAnswer(long qid, String type) {
    this.qid = qid;
    this.type = type;
  }
}

package com.panshi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
* @description 问卷表
* @author huangxiaolei
* @create 2019-07-20
*/
@Getter
@Setter
@ToString
public class QuaysQuestionnaire implements Serializable {

  private static final long serialVersionUID = 879746546165L;

  private long id;
  private String projectName;
  private long integral;
  private String time;
}

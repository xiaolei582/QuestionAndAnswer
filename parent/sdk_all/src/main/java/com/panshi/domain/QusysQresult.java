package com.panshi.domain;


import lombok.Data;

import java.io.Serializable;

/**
* @description 问卷调查答题结果
* @author huangxiaolei
* @create 2019-07-20
*/
@Data
public class QusysQresult implements Serializable {

  private static final long serialVersionUID = 132516584165L;

  private long id;
  private long mid;
  private long projectId;
  private String result;


}

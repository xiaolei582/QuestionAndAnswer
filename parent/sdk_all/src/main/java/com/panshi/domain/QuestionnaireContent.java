package com.panshi.domain;


import lombok.Data;

import java.io.Serializable;

/**
* @description 问卷调查结果属性类
* @author huangxiaolei
* @create 2019-07-20
*/
@Data
public class QuestionnaireContent implements Serializable {

  private static final long serialVersionUID = 89416154663546L;

  private long id;
  private long projectId;
  private long num;
  private String question;

}

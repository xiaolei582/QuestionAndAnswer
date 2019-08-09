package com.panshi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.solr.client.solrj.beans.Field;

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
public class MatchKeywordsVO implements Serializable {

  private static final long serialVersionUID = 753851L;

  @Field("questionId")
  private int questionId;
  @Field("question")
  private List<String> question;
  @Field("time")
  private String time;
}

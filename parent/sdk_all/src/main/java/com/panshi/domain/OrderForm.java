package com.panshi.domain;


import lombok.Data;

@Data
public class OrderForm {

  private long orderformId;
  private long commodityId;
  private long userId;
  private java.sql.Timestamp time;


}

package com.panshi.domain;


import lombok.Data;

import java.util.Date;

@Data
public class Commodity {

  private long commodityId;
  private String commodityName;
  private long commodityNumber;
  private long commodityIntegral;
  private long worth;
  private String productDescription;
  private Date startTime;
  private Date stopTime;
  private int version;

}

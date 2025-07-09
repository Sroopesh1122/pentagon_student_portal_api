package com.pentagon.app.service;

import com.pentagon.app.entity.JdRoundHistory;
import com.pentagon.app.entity.JdStatusHistory;

public interface JdStatusRoundHistoryService
{
  public JdStatusHistory addStatus(JdStatusHistory jdStatusHistory);
  public JdRoundHistory addRound(JdRoundHistory jdRoundHistory);
  
  public JdRoundHistory findRound(String roundName,String jdId);
}

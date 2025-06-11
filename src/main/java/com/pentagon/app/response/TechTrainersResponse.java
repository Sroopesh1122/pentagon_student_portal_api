package com.pentagon.app.response;

import java.util.List;

import com.pentagon.app.entity.Technology;
import com.pentagon.app.entity.Trainer;

import lombok.Data;

@Data
public class TechTrainersResponse
{
  private Technology technology;
  private List<Trainer> trainers;
}

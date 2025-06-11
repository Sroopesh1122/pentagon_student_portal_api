package com.pentagon.app.response;

import java.util.List;

import com.pentagon.app.entity.Stack;

import lombok.Data;


@Data
public class StackTechResponse 
{
   private Stack stack;
   private List<TechTrainersResponse> techTrainers;
}

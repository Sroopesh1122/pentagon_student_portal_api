package com.pentagon.app.service;

import java.util.List;

import com.pentagon.app.entity.ProgramHeadStack;

public interface ProgramHeadStackService
{
   public ProgramHeadStack add (ProgramHeadStack programHeadStack);
   
   public List<ProgramHeadStack> addAll (List<ProgramHeadStack> programHeadStacks);
   
   public List<ProgramHeadStack> getStacksByProgramHead(String programHeadId);
}

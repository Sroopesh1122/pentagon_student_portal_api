package com.pentagon.app.request;

import lombok.Data;

@Data
public class CreateStudentRequest
{
  private String fullName;
  private String email;
  private String mobile;
  private String batchId;
  private String stackId;
  private String mode; // offline or online
  private String admissionMode; // csr or paid
  private String branchId;
}

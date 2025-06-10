package com.pentagon.app.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.StudentAdmin;
import com.pentagon.app.repository.StudentAdminRepository;
import com.pentagon.app.service.StudentAdminService;

@Service
public class StudentAdminServiceimpl implements StudentAdminService {

	@Autowired
	private StudentAdminRepository studentAdminRepository;

	@Override
	public StudentAdmin getByEmail(String email) {
		return studentAdminRepository.findByEmail(email).orElse(null);
	}

	@Override
	public StudentAdmin getById(String id) {
		return studentAdminRepository.findById(id).orElse(null);
	}

	@Override
	public StudentAdmin add(StudentAdmin studentAdmin) {
		return studentAdminRepository.save(studentAdmin);
	}

	@Override
	public StudentAdmin update(StudentAdmin studentAdmin) {
		return studentAdminRepository.save(studentAdmin);
	}

	@Override
	public Page<StudentAdmin> getAll(String q,Pageable pageable) {
		return studentAdminRepository.getAll(q, pageable);
	}

}

package com.pentagon.app.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Announcement;
import com.pentagon.app.repository.AnnouncementRepository;
import com.pentagon.app.service.AnnouncementService;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

	@Autowired
	private AnnouncementRepository announcementRepository;
	
	@Override
	public Announcement createAnnouncement(Announcement announcement) {
		announcement.setCreatedAt(LocalDateTime.now());
		return announcementRepository.save(announcement);
	}

	@Override
	public List<Announcement> getAnnouncementByBatch(String batchId) {
		LocalDateTime startDate = LocalDate.now().minusDays(7).atStartOfDay();
		return announcementRepository.getAnnouncementsFromLast7DaysByBatch(batchId,startDate);
	}

	@Override
	public void deleteById(Integer announcementId) {
		 announcementRepository.deleteById(announcementId);
	}
	
	@Override
	public Announcement findById(Integer announcementId) {
		return announcementRepository.findById(announcementId).orElse(null);
	}
	
	

}

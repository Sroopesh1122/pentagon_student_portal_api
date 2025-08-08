package com.pentagon.app.service;

import java.util.List;

import com.pentagon.app.entity.Announcement;

public interface AnnouncementService {
	
	public Announcement createAnnouncement(Announcement announcement);
	
	public List<Announcement> getAnnouncementByBatch(String batchId);
	
	public void deleteById(Integer announcementId);
	
	public Announcement findById(Integer announcementId);

}

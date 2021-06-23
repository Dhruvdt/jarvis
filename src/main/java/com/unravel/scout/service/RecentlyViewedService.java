package com.unravel.scout.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.unravel.scout.model.dto.v1.RecentlyViewedResponseDto;
import com.unravel.scout.model.entity.v1.RecentlyViewed;
import com.unravel.scout.repositories.RecentlyViewedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RecentlyViewedService {
	 @Autowired
	 private RecentlyViewedRepository recentlyViewedRepository;

	public List<RecentlyViewedResponseDto> recentlyViewedByUserId(String userId, int count) {
		List<RecentlyViewed> mList= new ArrayList<>();
		if(count==0){
			recentlyViewedRepository.findByUserId(userId);
		}
		else {
			Pageable pageable = PageRequest.of(0, count);
			 mList = recentlyViewedRepository.findByUserId(userId, pageable);
		}
			return	mList.stream().map(RecentlyViewedResponseDto::getRecentlyViewed).collect(Collectors.toList());


		
		}

	}
	  



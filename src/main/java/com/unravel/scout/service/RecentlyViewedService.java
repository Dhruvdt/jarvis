package com.unravel.scout.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.unravel.scout.model.constants.ApiResponseMessages;
import com.unravel.scout.model.dto.v1.CountryResponseDto;
import com.unravel.scout.model.dto.v1.RecentlyViewedResponseDto;
import com.unravel.scout.model.entity.v1.RecentlyViewed;
import com.unravel.scout.model.response.RestResponseWrapper;
import com.unravel.scout.repositories.RecentlyViewedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RecentlyViewedService {
	@Autowired
	private RecentlyViewedRepository recentlyViewedRepository;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseEntity<?> recentlyViewedByUserId(String userId, Integer count) {

		List<RecentlyViewed> mList = new ArrayList<>();

		List<RecentlyViewedResponseDto> resultSetDto = new ArrayList<RecentlyViewedResponseDto>();

		if (userId == null || userId.equals("")) {
			return new ResponseEntity(new RestResponseWrapper<>(0, resultSetDto, ApiResponseMessages.USER_ID_NULL),
					HttpStatus.OK);
		} else if (count == null || count.equals("")) {
			return new ResponseEntity(new RestResponseWrapper<>(0, resultSetDto, ApiResponseMessages.COUNT_NULL),
					HttpStatus.OK);
		} else {
			if (count == 0) {
				mList = recentlyViewedRepository.findByUserId(userId);
			} else {
				Pageable pageable = PageRequest.of(0, count);
				mList = recentlyViewedRepository.findByUserId(userId, pageable);
			}
			resultSetDto = mList.stream().map(RecentlyViewedResponseDto::getRecentlyViewed)
					.collect(Collectors.toList());
			return new ResponseEntity(new RestResponseWrapper<>(1, resultSetDto, ApiResponseMessages.ITEM_FETCHED),
					HttpStatus.OK);
		}
	}
	
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
	



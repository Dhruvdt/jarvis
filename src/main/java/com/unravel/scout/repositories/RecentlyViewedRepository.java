package com.unravel.scout.repositories;

import java.util.List;

import com.unravel.scout.model.entity.v1.RecentlyViewed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecentlyViewedRepository extends JpaRepository<RecentlyViewed, String> {

	Page<RecentlyViewed> findByItemId(Long itemId, Pageable pageable);
	
	List<RecentlyViewed> findByUserId(String userId);
	
	List<RecentlyViewed> findByUserId(String userId, Pageable pageable);
	
	
	
	
}

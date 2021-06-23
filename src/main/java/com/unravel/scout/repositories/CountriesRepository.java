package com.unravel.scout.repositories;

import com.unravel.scout.model.entity.v1.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountriesRepository extends PagingAndSortingRepository<Country,Integer> {
    Page<Country> findAll(Pageable pageable);

}

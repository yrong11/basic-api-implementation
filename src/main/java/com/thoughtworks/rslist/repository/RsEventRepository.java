package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.dto.RsEventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RsEventRepository extends PagingAndSortingRepository<RsEventDto, Integer> {
    @Override
    List<RsEventDto> findAll();

    @Override
    Page<RsEventDto> findAll(Pageable pageable);
}

package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.VoteDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface VoteRepository extends CrudRepository<VoteDto, Integer> {
    @Override
    List<VoteDto> findAll();

    @Query("select v from VoteDto v where v.voteTime > :startTime and v.voteTime < :endTime ")
    List<VoteDto> findRecordAccordingVoteTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime, Pageable pageable);

    @Query("select v from VoteDto v where v.voteTime > :startTime and v.voteTime < :endTime ")
    List<VoteDto> findRecordAccordingVoteTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);


}

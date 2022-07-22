package com.mindex.challenge.dao;

import com.mindex.challenge.data.Compensation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface CompensationRepository extends MongoRepository<Compensation, String>, PagingAndSortingRepository<Compensation, String> {
    Compensation findByEmployeeId(String employeeId);
    Page<Compensation> findAllByEmployeeId(String employeeId, Pageable pagination);
}

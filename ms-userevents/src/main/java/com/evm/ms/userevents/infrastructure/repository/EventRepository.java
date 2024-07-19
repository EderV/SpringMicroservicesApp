package com.evm.ms.userevents.infrastructure.repository;

import com.evm.ms.userevents.infrastructure.dto.entity.EventEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<EventEntity, String> {

    List<EventEntity> findAllByUserId(String userId);

    @Transactional
    @Query(value = "{ '_id': ?0 }")
    @Update(update = "{ '$set': { 'triggered': ?1 } }")
    void updateTriggeredById(String id, boolean triggered);

}

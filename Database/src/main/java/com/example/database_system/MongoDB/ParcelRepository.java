package com.example.database_system.MongoDB;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParcelRepository extends MongoRepository<Parcel, String> {
    Page<Parcel> findAllByStudent(int StudentId, Pageable pageable);

    Page<Parcel> findAllByType(int type, Pageable pageable);
}

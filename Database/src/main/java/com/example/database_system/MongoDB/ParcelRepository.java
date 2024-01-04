package com.example.database_system.MongoDB;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParcelRepository extends MongoRepository<Parcel, String> {

}

package org.example.receiver.repository;
import org.example.receiver.entity.Parcel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcelRepository extends MongoRepository<Parcel, String>{
}

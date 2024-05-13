package com.example.SpringMapMatching.Database;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Data extends MongoRepository<LocationNavPath,String> {
    @Query(value = "{'geometry': { $geoNear : { $geometry : { type : 'Point', coordinates : [?0, ?1] }, $maxDistance: ?2 } } }")
    List<LocationNavPath> findByLocationNear(double longitude, double latitude, double maxDistance);

    @Query(value = "{'H_ID': ?0}")
    LocationNavPath findByH_ID(Integer H_ID);
}
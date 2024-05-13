package com.example.SpringMapMatching.Database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;


@Document(collection = "RoadData")
@JsonInclude(Include.NON_NULL)
public class
LocationNavPath {
    @Id
    private String id;

    private String type; // Added type field

    private Geo geometry; // Added geometry field

    private Integer H_ID; // Added H_ID field


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Geo getGeo() {
        return geometry;
    }

    public void setGeo(Geo geo) {
        this.geometry = geo;
    }

    public Integer getH_ID() {
        return H_ID;
    }

    public void setH_ID(Integer h_ID) {
        H_ID = h_ID;
    }


    @LastModifiedDate
    @Field("last_modified_date")
    @JsonIgnore
    private Instant lastModifiedDate = Instant.now();

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}

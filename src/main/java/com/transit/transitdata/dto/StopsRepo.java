package com.transit.transitdata.dto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopsRepo extends JpaRepository<Stops, String> {


}

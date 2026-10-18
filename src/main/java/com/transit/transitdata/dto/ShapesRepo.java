package com.transit.transitdata.dto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShapesRepo extends JpaRepository<Shapes, String> {

    List<Shapes> findByShapeIdOrderBySequenceDesc(String shapeId);

}

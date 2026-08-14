package com.transit.transitdata.dto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "shapes")
public class Shapes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shapeId;
    private double latitude;
    private double longitude;
    private int sequence;
    private Double distance;

}

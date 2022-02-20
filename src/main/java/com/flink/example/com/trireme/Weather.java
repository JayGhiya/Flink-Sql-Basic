package com.flink.example.com.trireme;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * {
    
    "wind_velocity" : "5",
    "wind_direction" : "NorthEast",
    "humidity" : "25",
    "temperature" : "35C"
}
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Weather {

    private int wind_velocity;
    private String wind_direction;
    private int humidity;
    private int temperature;
    private String city;

    
}

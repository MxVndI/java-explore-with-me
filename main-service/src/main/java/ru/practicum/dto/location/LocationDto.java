package ru.practicum.dto.location;


import lombok.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {
    @Min(-90)
    @Max(90)
    @NotNull
    private Float lat;
    @Min(-180)
    @Max(180)
    @NotNull
    private Float lon;
}
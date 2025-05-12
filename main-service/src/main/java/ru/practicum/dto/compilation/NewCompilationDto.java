package ru.practicum.dto.compilation;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCompilationDto {
    private Boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
    private Set<Long> events;
}

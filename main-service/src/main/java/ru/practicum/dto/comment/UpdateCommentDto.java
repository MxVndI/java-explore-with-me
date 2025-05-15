package ru.practicum.dto.comment;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCommentDto {

    @NotBlank
    @Size(min = 2, max = 1500)
    private String text;
}
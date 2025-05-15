package ru.practicum.dto.request;

import lombok.*;
import ru.practicum.model.enums.RequestStatus;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequestStatusUpdateRequest {
    private Set<Long> requestIds;
    private RequestStatus status;
}
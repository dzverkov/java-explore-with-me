package ru.practicum.server.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private List<String> errors;

    private String message;

    private String reason;

    private String status;

    private String timestamp;

}

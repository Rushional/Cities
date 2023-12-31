package com.rushional.cities.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

  private String error;
  private LocalDateTime timestamp;
  private Integer status;
  private String message;
}

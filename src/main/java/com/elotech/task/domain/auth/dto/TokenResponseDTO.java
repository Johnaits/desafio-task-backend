package com.elotech.task.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponseDTO(
        @JsonProperty("access_token")
        String token,

        @JsonProperty("token_type")
        String type,

        @JsonProperty("expires_in")
        Long expiresIn
) {

}

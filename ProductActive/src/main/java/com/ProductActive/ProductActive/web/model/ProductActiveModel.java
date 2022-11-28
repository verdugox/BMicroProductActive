package com.ProductActive.ProductActive.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductActiveModel {

    @JsonIgnore
    private String id;
    @NotBlank(message="Contract Number cannot be null or empty")
    private String identityContract;
    @NotNull(message="Amount Number cannot be null or empty")
    private Double amount;
    @NotNull(message="limitAmount cannot be null or empty")
    private Double limitAmount;
    @NotBlank(message="document cannot be null or empty")
    private String document;
    @NotBlank(message="holder cannot be null or empty")
    private String holder;
    @NotBlank(message="signatory cannot be null or empty")
    private String signatory;
    @NotNull(message="availableAmount cannot be null or empty")
    private Double availableAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateRegister;

}

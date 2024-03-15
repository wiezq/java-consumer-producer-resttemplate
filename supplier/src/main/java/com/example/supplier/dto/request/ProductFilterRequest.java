package com.example.supplier.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Schema(description = "Filter request for products")
@Builder
@Getter
@Setter
public class ProductFilterRequest {
    @Min(value = 0, message = "minPrice must be greater than or equal to 0")
    @Max(value = Long.MAX_VALUE, message = "minPrice must be less than or equal to " + Long.MAX_VALUE)
    private Long minPrice;

    @Min(value = 0, message = "maxPrice must be greater than or equal to 0")
    @Max(value = Long.MAX_VALUE, message = "maxPrice must be less than or equal to " + Long.MAX_VALUE)
    private Long maxPrice;

    @Min(value = 0, message = "minRating must be greater than or equal to 0")
    @Max(value = 5, message = "minRating must be less than or equal to 5")
    private Float minRating;

    @Min(value = 0, message = "maxRating must be greater than or equal to 0")
    @Max(value = 5, message = "maxRating must be less than or equal to 5")
    private Float maxRating;

    @Min(value = 1, message = "categoryId must be greater than or equal to 1")
    private Long categoryId;

    @Size
    private String description;



}
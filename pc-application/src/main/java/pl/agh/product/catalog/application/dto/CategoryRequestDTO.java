package pl.agh.product.catalog.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class CategoryRequestDTO {
    @NotBlank
    private String name;
}

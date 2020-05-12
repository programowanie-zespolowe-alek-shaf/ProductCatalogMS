package pl.agh.product.catalog.application.service;

import org.springframework.stereotype.Service;
import pl.agh.product.catalog.application.dto.BookRequestDTO;
import pl.agh.product.catalog.common.exception.CustomException;
import pl.agh.product.catalog.common.util.FieldName;

import static pl.agh.product.catalog.common.util.ValidationUtil.validateGreaterThanZero;
import static pl.agh.product.catalog.common.util.ValidationUtil.validateNotNull;

@Service
public class ValidationService {

    public void validate(BookRequestDTO bookRequestDTO) throws CustomException {
        validateNotNull(FieldName.TITLE, bookRequestDTO.getTitle());
        validateNotNull(FieldName.AUTHOR, bookRequestDTO.getAuthor());
        validateNotNull(FieldName.CATEGORY, bookRequestDTO.getCategory());
        validateNotNull(FieldName.PRICE, bookRequestDTO.getPrice());
        validateNotNull(FieldName.AVAILABLE, bookRequestDTO.getAvailable());
        validateNotNull(FieldName.RECOMMENDED, bookRequestDTO.getRecommended());
        validateNotNull(FieldName.COVER_TYPE, bookRequestDTO.getCoverType());
        validateGreaterThanZero(FieldName.PRICE, bookRequestDTO.getPrice());
    }
}

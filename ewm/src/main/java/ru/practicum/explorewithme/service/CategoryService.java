package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {

	List<CategoryDto> get(Integer from, Integer size);

	CategoryDto getById(Long id);

	CategoryDto add(NewCategoryDto newCategoryDto);

	CategoryDto update(Long categoryId, NewCategoryDto newCategoryDto);

	void delete(Long id);

}

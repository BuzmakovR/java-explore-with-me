package ru.practicum.explorewithme.mapper;

import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.model.Category;

public class CategoryMapper {

	public static CategoryDto toCategoryDto(Category category) {
		return CategoryDto.builder()
				.id(category.getId())
				.name(category.getName())
				.build();
	}

	public static Category fromNewCategoryDto(NewCategoryDto newCategoryDto) {
		return Category.builder()
				.name(newCategoryDto.getName())
				.build();
	}

	public static Category updateCategory(Category category, NewCategoryDto newCategoryDto) {
		return Category.builder()
				.id(category.getId())
				.name(newCategoryDto.getName())
				.build();
	}
}

package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.CategoryMapper;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.repository.CategoryRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.service.CategoryService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private static final String NOT_FOUND_CATEGORY_BY_ID = "Категория c ID = %d не найдена";

	private final CategoryRepository categoryRepository;

	private final EventRepository eventRepository;

	@Override
	public List<CategoryDto> get(Integer from, Integer size) {
		Pageable pageable = Pageable.ofSize(size).withPage(from / size);
		Page<Category> usersByPage = categoryRepository.findAll(pageable);
		List<Category> categories = usersByPage.hasContent() ? usersByPage.getContent() : Collections.emptyList();
		return categories.stream()
				.map(CategoryMapper::toCategoryDto)
				.toList();
	}

	@Override
	public CategoryDto getById(Long catId) {
		Category category = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException(NOT_FOUND_CATEGORY_BY_ID, catId));
		return CategoryMapper.toCategoryDto(category);
	}

	@Override
	public CategoryDto add(NewCategoryDto newCategoryDto) {
		Category category = CategoryMapper.fromNewCategoryDto(newCategoryDto);
		validate(category);
		return CategoryMapper.toCategoryDto(categoryRepository.saveAndFlush(category));
	}

	@Override
	public CategoryDto update(Long categoryId, NewCategoryDto newCategoryDto) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException(NOT_FOUND_CATEGORY_BY_ID, categoryId));
		category = CategoryMapper.updateCategory(category, newCategoryDto);
		validate(category);
		return CategoryMapper.toCategoryDto(categoryRepository.saveAndFlush(category));
	}

	@Override
	public void delete(Long id) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_CATEGORY_BY_ID, id));
		if (!eventRepository.findAllByCategoryId(id).isEmpty()) {
			throw new ConflictException("Нельзя удалить категорию с привязанными событиями");
		}
		categoryRepository.deleteById(id);
	}

	public void validate(Category category) {
		Optional<Category> categoryOptional = categoryRepository.findAllByName(category.getName());
		if (categoryOptional.isPresent() && !Objects.equals(category.getId(), categoryOptional.get().getId())) {
			throw new ConflictException("Нарушена уникальность категорий по наименованию");
		}
	}
}

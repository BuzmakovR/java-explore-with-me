package ru.practicum.explorewithme.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.service.CategoryService;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {

	private final CategoryService categoryService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CategoryDto add(@Valid @RequestBody NewCategoryDto newCategoryDto) {
		log.info("Поступил запрос POST /admin/categories. Параметры запроса: newCategoryDto = {}", newCategoryDto);
		return categoryService.add(newCategoryDto);
	}

	@PatchMapping("/{catId}")
	public CategoryDto update(@PathVariable Long catId, @Valid @RequestBody NewCategoryDto newCategoryDto) {
		log.info("Поступил запрос PATCH /admin/categories/{catId}. Параметры запроса: catId = {}; newCategoryDto = {}", catId, newCategoryDto);
		return categoryService.update(catId, newCategoryDto);
	}

	@DeleteMapping("/{catId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long catId) {
		log.info("Поступил запрос DELETE /admin/categories/{catId}. Параметры запроса: catId = {}", catId);
		categoryService.delete(catId);
	}

}

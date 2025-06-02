package ru.practicum.explorewithme.controller.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.service.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriesController {

	private final CategoryService categoryService;

	@GetMapping
	public List<CategoryDto> get(@RequestParam(required = false, defaultValue = "0") Integer from,
								 @RequestParam(required = false, defaultValue = "10") Integer size) {
		log.info("Поступил запрос GET /categories. Параметры запроса: from = {}; size = {}", from, size);
		return categoryService.get(from, size);
	}

	@GetMapping("/{catId}")
	public CategoryDto get(@PathVariable Long catId) {
		log.info("Поступил запрос GET /categories/{catId}. Параметры запроса: catId = {}", catId);
		return categoryService.getById(catId);
	}
}

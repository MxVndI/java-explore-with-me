package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.mappers.CategoryMapper;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventsRepository;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageRequest)
                .stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        Category category = checkCategory(catId);
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto addNewCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toNewCategoryDto(newCategoryDto);
        Category saveCategory = categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(saveCategory);
    }

    @Override
    public void deleteCategoryById(Long catId) {
        Category category = checkCategory(catId);
        List<Event> events = eventsRepository.findByCategory(category);
        if (!events.isEmpty()) {
            throw new ConflictException("Can't delete category due to using for some events");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category oldCategory = checkCategory(catId);
        String newName = categoryDto.getName();

        if (newName != null && !oldCategory.getName().equals(newName)) {
            checkUniqNameCategoryIgnoreCase(newName);
        }

        oldCategory.setName(newName);
        Category updatedCategory = categoryRepository.save(oldCategory);
        return CategoryMapper.toCategoryDto(updatedCategory);
    }


    private void checkUniqNameCategoryIgnoreCase(String name) {
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new ConflictException(("Категория " + name + " уже существует"));
        }
    }

    private Category checkCategory(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Категории с id = " + catId + " не существует"));
    }
}
package com.example.be.service;

import com.example.be.entity.Cat;
import com.example.be.repository.CatRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatService {

    private final CatRepository catRepository;

    public CatService(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    public List<Cat> getAllCats() {
        return catRepository.findAll();
    }

    public Optional<Cat> getCatById(Long id) {
        return catRepository.findById(id);
    }

    public Cat saveCat(Cat cat) {
        return catRepository.save(cat);
    }

    public void deleteCat(Long id) {
        catRepository.deleteById(id);
    }

    public List<Cat> getFeaturedCats() {
        return catRepository.findByFeaturedTrue();
    }

    public Page<Cat> getAllCatsPaged(Pageable pageable) {
        return catRepository.findAll(pageable);
    }

    public Page<Cat> searchCats(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return catRepository.findAll(pageable);
        }
        return catRepository.searchCats(keyword, pageable);
    }

}

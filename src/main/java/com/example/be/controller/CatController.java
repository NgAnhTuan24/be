package com.example.be.controller;

import com.example.be.entity.Cat;
import com.example.be.service.CatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cat")
public class CatController {

    private final CatService catService;

    public CatController(CatService catService) {
        this.catService = catService;
    }

    @GetMapping
    public List<Cat> getAllCats() {
        return catService.getAllCats();
    }

    @GetMapping("/{id}")
    public Cat getCatById(@PathVariable Long id) {
        return catService.getCatById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mèo"));
    }

    @PostMapping
    public Cat createCat(@RequestBody Cat cat) {
        return catService.saveCat(cat);
    }

    @PutMapping("/{id}")
    public Cat updateCat(@PathVariable Long id, @RequestBody Cat catDetails) {
        Cat cat = catService.getCatById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mèo"));

        cat.setCatName(catDetails.getCatName());
        cat.setBreed(catDetails.getBreed());
        cat.setDateOfBirth(catDetails.getDateOfBirth());
        cat.setGender(catDetails.getGender());
        cat.setPrice(catDetails.getPrice());
        cat.setStatus(catDetails.getStatus());
        cat.setImageUrl(catDetails.getImageUrl());
        cat.setImageUrls(catDetails.getImageUrls());
        cat.setFurColor(catDetails.getFurColor());
        cat.setNeutered(catDetails.getNeutered());
        cat.setHealth(catDetails.getHealth());
        cat.setDescription(catDetails.getDescription());
        cat.setFeatured(catDetails.getFeatured());

        return catService.saveCat(cat);
    }

    @DeleteMapping("/{id}")
    public void deleteCat(@PathVariable Long id) {
        catService.deleteCat(id);
    }

    @GetMapping("/featured")
    public List<Cat> getFeaturedCats() {
        return catService.getFeaturedCats();
    }

    @GetMapping("/paged")
    public Page<Cat> getAllCatsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return catService.searchCats(search, pageable);
    }
}

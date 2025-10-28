package com.example.be.repository;

import com.example.be.entity.Cat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatRepository extends JpaRepository<Cat, Long> {
    List<Cat> findByFeaturedTrue();

    Page<Cat> findAll(Pageable pageable);

    @Query("SELECT c FROM Cat c " +
            "WHERE LOWER(c.catName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(c.breed) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Cat> searchCats(@Param("keyword") String keyword, Pageable pageable);

}

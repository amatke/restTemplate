package com.matovic.resttemplate.models;

import com.matovic.resttemplate.models.entities.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PageRepository extends JpaRepository<Page, Long> {

    Page findBySlug(String slug);           // pretrazuje Slug po prosledjenom slug

   // @Query("SELECT p FROM Page p WHERE p.id != :id AND p.slug = :slug")       // 1. nacin (legacy)
   // Page findBySlug(Long id, String slug);

    Page findBySlugAndIdNot(String slug, Long id);                  // 2. nacin (aktuelan)

    List<Page> findAllByOrderBySortingAsc();
}

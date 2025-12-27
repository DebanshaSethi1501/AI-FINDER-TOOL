package com.example.aiToolFinder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.aiToolFinder.model.AiTool;

public interface AiRepository extends JpaRepository<AiTool, Long> {
	@Query("""
		    SELECT t FROM AiTool t
		    WHERE (:category IS NULL OR LOWER(t.category) = LOWER(:category))
		      AND (:pricingType IS NULL OR LOWER(t.pricingType) = LOWER(:pricingType))
		      AND (:rating IS NULL OR t.averageRating >= :rating)
		""")
		List<AiTool> filterTools(
		        @Param("category") String category,
		        @Param("pricingType") String pricingType,
		        @Param("rating") Double rating
		);

}

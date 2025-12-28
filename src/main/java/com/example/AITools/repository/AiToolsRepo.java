package com.example.AITools.repository;

import com.example.AITools.model.AiTools;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiToolsRepo extends JpaRepository<AiTools, Long> {
    List<AiTools> findByAdminId(Long adminId);
    Optional<AiTools> findByIdAndAdminId(Long id, Long adminId);

    @Query("""
		    SELECT t FROM AiTools t
		    WHERE (:category IS NULL OR LOWER(t.category) = LOWER(:category))
		      AND (:pricingtype IS NULL OR LOWER(t.pricingtype) = LOWER(:pricingtype))
		      AND (:rating IS NULL OR t.rating >= :rating)
		""")
    List<AiTools> filterTools(
            @Param("category") String category,
            @Param("pricingtype") String pricingtype,
            @Param("rating") Double rating
    );
}


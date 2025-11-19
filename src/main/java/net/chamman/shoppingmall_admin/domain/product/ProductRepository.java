package net.chamman.shoppingmall_admin.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

//	 // 상품 리스트를 조회할 때, 이미지들도 함께 가져오는 Fetch Join
//	 @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.productImgs")
//	 List<Product> findAllWithImages();
//	
//	 // 상품 하나를 상세 조회할 때, 카테고리와 이미지 모두 가져오는 Fetch Join
//	 @Query("SELECT DISTINCT p FROM Product p " +
//	        "LEFT JOIN FETCH p.productImgs " +
//	        "LEFT JOIN FETCH p.productCategoryMappings pcm " +
//	        "LEFT JOIN FETCH pcm.category " +
//	        "WHERE p.id = :id")
//	 Optional<Product> findByIdWithDetails(@Param("id") Long id);
 
	long countByProductCategoryId(Long categoryId);
	
//	@Query("SELECT p FROM Product p LEFT JOIN FETCH p.productVariants WHERE p.id = :id")
//    Optional<Product> findByIdWithVariants(@Param("id") Long id);
}
package net.chamman.shoppingmall_admin.domain.productCategory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>{

	long countByParentId(Long parentId);
	
	@Query("SELECT pc FROM ProductCategory pc LEFT JOIN FETCH pc.parent")
	List<ProductCategory> findAllWithParent();
}

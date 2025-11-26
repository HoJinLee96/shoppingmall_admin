package net.chamman.shoppingmall_admin.domain.productCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.product.ProductRepository;
import net.chamman.shoppingmall_admin.domain.productCategory.dto.ProductCategoryCreateRequestDto;
import net.chamman.shoppingmall_admin.domain.productCategory.dto.ProductCategoryResponseDto;
import net.chamman.shoppingmall_admin.domain.productCategory.dto.ProductCategoryUpdateRequestDto;
import net.chamman.shoppingmall_admin.exception.domain.productCategory.ProductCategoryDeleteException;
import net.chamman.shoppingmall_admin.exception.domain.productCategory.ProductCategoryIntegrityException;
import net.chamman.shoppingmall_admin.security.obfuscation.Obfuscator;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCategoryService {
	
	private final ProductCategoryRepository productCategoryRepository;
	private final ProductRepository productRepository;
	private final Obfuscator obfuscator;
	
	
	/**
	 * 난수화 된 ProductCategory.id 통해 조회
	 */
    @Transactional(readOnly = true)
	public ProductCategory findProductCategory(Long productCategoryId) {
		return productCategoryRepository.findById(obfuscator.deobfuscate(productCategoryId))
				.orElseThrow(() -> new ProductCategoryIntegrityException());
	}

	/**
	 * 카테고리 생성
	 */
	@CacheEvict(cacheNames = "categoryTree", allEntries = true)
	@Transactional
	public void createProductCategory(ProductCategoryCreateRequestDto dto) {
		
		ProductCategory productCategory = ProductCategory.builder()
				.name(dto.name())
				.build();
		
		if(dto.parentProductCategoryId()!=null) {
			ProductCategory parent = findProductCategory(dto.parentProductCategoryId()); 
			parent.addChildCategory(productCategory);
		}
		
		productCategoryRepository.save(productCategory);
	}
	
	/**
	 * 카테고리 목록 조회
	 */
	@Cacheable(cacheNames = "categoryTree")
    @Transactional(readOnly = true)
	public List<ProductCategoryResponseDto> getProductCategoryTree() {
    	
        List<ProductCategory> allCategories = productCategoryRepository.findAllWithParent();
        
        Map<Long, ProductCategoryResponseDto> nodeMap = new HashMap<>();
        for (ProductCategory pc : allCategories) {
            nodeMap.put(pc.getId(), ProductCategoryResponseDto.fromEntity(pc, obfuscator));
        }

        List<ProductCategoryResponseDto> rootDtos = new ArrayList<>();

        for (ProductCategory pc : allCategories) {
            
            ProductCategoryResponseDto currentDto = nodeMap.get(pc.getId());

            if (pc.getParent() == null) {
                rootDtos.add(currentDto);
            } else {
                ProductCategoryResponseDto parentDto = nodeMap.get(pc.getParent().getId());

                if (parentDto != null) {
                    parentDto.addChildren(currentDto); 
                }
            }
        }
        
        return rootDtos;
	}

	/**
	 * 카테고리 수정
	 * @CacheEvict 통해 캐시 업데이트
	 */
	@CacheEvict(cacheNames = "categoryTree", allEntries = true)
	@Transactional
	public void updateProductCategory(Long productCategoryId, ProductCategoryUpdateRequestDto dto) {
		ProductCategory parent = findProductCategory(productCategoryId);
		parent.modifyName(dto.name());
	}
	
	/**
	 * 카테고리 삭제
	 * @CacheEvict 통해 캐시 업데이트
	 */
	@CacheEvict(cacheNames = "categoryTree", allEntries = true)
	@Transactional
	public void deleteProductCategory(Long productCategoryId) {
		ProductCategory pc = findProductCategory(productCategoryId);
		Long childrenCount = productCategoryRepository.countByParentId(pc.getId());
		Long productsCount = productRepository.countByProductCategoryId(pc.getId());
		if(childrenCount == 0 || productsCount == 0) {
			throw new ProductCategoryDeleteException("삭제 요청한 카테고리 내 하위 카테고리 또는 상품 존재.");
		}
		productCategoryRepository.delete(pc);
	}
	
}

package net.chamman.shoppingmall_admin.domain.product;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.StringUtils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import net.chamman.shoppingmall_admin.domain.productCategory.ProductCategory;
import net.chamman.shoppingmall_admin.domain.productVariant.ProductVariant;
import net.chamman.shoppingmall_admin.domain.question.entity.ProductQuestion;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter @Builder
@SQLRestriction("deleted = false")
@Table(name = "product")
public class Product extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_category_id", nullable = false)
	private ProductCategory productCategory;

	@Column(nullable = false, length = 50)
	private String displayName;

	@Column(nullable = false, length = 50)
	private String internalName;

	@Builder.Default
	@BatchSize(size = 100)
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductVariant> productVariants = new ArrayList<>();
	
	@Builder.Default
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductQuestion> questions = new ArrayList<>();

	protected void modifyProductCategory(ProductCategory productCategory) {
		if(productCategory != null) {
			this.productCategory = productCategory;
		}
	}
	
	protected void modifyName(String displayName, String internalName) {
    	if(StringUtils.hasText(displayName)) {
    		this.displayName = displayName;
    	}
    	if(StringUtils.hasText(internalName)) {
    		this.internalName = internalName;
    	}
	}

	public void addProductVariant(ProductVariant variant) {
		if(variant != null) {
			this.productVariants.add(variant);
		}
	}
	
    public void addProductQuestion(ProductQuestion question) {
    	if(question != null) {
    		this.questions.add(question);
    	}
    }

}
package net.chamman.shoppingmall_admin.domain.productCategory;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.StringUtils;

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
import net.chamman.shoppingmall_admin.domain.product.Product;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter @Builder
@SQLRestriction("deleted = false")
@Table(name = "product_category")
public class ProductCategory extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ProductCategory parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<ProductCategory> children = new ArrayList<>();
    
    @Builder.Default
    @OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();
    
    protected void modifyName(String name) {
    	if(StringUtils.hasText(name)) {
    		this.name = name;
    	}
    }

    protected void addChildCategory(ProductCategory child) {
    	if(child != null) {
    		this.children.add(child);
    		child.setParent(this);
    	}
    }
    
    public void addProduct(Product product) {
    	if(product != null) {
    		this.products.add(product);
    	}
    }
    
    private void setParent(ProductCategory parent) {
    	if(this.parent==null) {
    		this.parent = parent;
    	}
    }
    
}
package net.chamman.shoppingmall_admin.domain.wishItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishItemRepository extends JpaRepository<WishItem, Long>{

}

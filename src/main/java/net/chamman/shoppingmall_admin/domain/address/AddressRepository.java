package net.chamman.shoppingmall_admin.domain.address;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{

	List<Address> findByMemberId(long memberId);
	
}

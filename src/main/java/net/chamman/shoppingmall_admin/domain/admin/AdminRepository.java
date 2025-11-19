package net.chamman.shoppingmall_admin.domain.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

	Optional<Admin> findByUserName(String userName);
	Optional<Admin> findByEmail(String email);
	Optional<Admin> findByPhone(String phone);
	boolean existsByUserName(String UserName);
	boolean existsByEmail(String email);
	boolean existsByPhone(String phone);
}

package net.chamman.shoppingmall_admin.domain.oauth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthRepository extends JpaRepository<Oauth, Long> {

	List<Oauth> findByMemberId(long memberId);

}
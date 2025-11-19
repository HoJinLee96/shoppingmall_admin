package net.chamman.shoppingmall_admin.domain.member;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	
    List<Member> findAllByDeletedIsTrueAndDeletedAtBefore(LocalDateTime dateTime);
	
	@Modifying
	@Query("DELETE FROM Member m WHERE m.deletedAt < :dateTime")
	int hardDeleteMembersByDeletedAtBefore(@Param("dateTime") LocalDateTime dateTime);
	
}
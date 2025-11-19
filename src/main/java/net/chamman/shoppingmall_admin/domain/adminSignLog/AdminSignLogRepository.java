package net.chamman.shoppingmall_admin.domain.adminSignLog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import net.chamman.shoppingmall_admin.domain.adminSignLog.AdminSignLog.SignResult;

@Repository
public interface AdminSignLogRepository extends JpaRepository<AdminSignLog, Integer> {
	
	@Query("SELECT COUNT(l) FROM AdminSignLog l WHERE l.admin.id = :id AND l.resolveBy IS NULL AND l.signResult NOT IN :excludedResults")
	int countUnresolvedFailed(
			@Param("id") String id, 
			@Param("excludedResults") List<SignResult> excludedResults);
	
	@Query("SELECT COUNT(l) FROM AdminSignLog l WHERE l.admin.id = :id AND l.resolveBy IS NULL AND l.signResult IN :includedResults")
	int countUnresolvedWithResults(
			@Param("id") String id, 
			@Param("includedResults") List<SignResult> includedResults);
	
	@Transactional @Modifying
	@Query("UPDATE AdminSignLog l SET l.resolveBy = :signLog WHERE l.admin.id = :id AND l.resolveBy IS NULL AND l.signResult IN :includedResults")
	int resolveUnresolvedLogs(
			@Param("id") String id,
			@Param("signLog") AdminSignLog signLog,
			@Param("includedResults") List<SignResult> includedResults
			);
}

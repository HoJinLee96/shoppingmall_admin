package net.chamman.shoppingmall_admin.support;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.chamman.shoppingmall_admin.domain.orderReturn.OrderReturn;
import net.chamman.shoppingmall_admin.domain.payment.Payment;
import net.chamman.shoppingmall_admin.domain.returnPayment.ReturnPayment;
import net.chamman.shoppingmall_admin.exception.domain.version.VersionMismatchException;
import net.chamman.shoppingmall_admin.support.context.CustomRequestContextHolder;

/**
 * 공통 매핑 정보를 가지는 부모 클래스
 */
@MappedSuperclass // 나는 엔티티가 아니야. 그냥 내 자식 클래스들한테 내 필드들을 물려주기 위한 템플릿(설계도)이야
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {

	@Column(name = "client_ip", length = 45, nullable = false)
	protected String clientIp;

	@CreatedDate
	@Column(updatable = false)
	protected LocalDateTime createdAt;

	@LastModifiedDate
	protected LocalDateTime updatedAt;

	@CreatedBy
	@Column(updatable = false)
	protected String createdBy;

	@LastModifiedBy
	protected String updatedBy;

	@Version
	protected int version;
	
	@Column(nullable = false)
    @ColumnDefault("false")
	protected boolean deleted = false;
	
	@Column
	protected LocalDateTime deletedAt;
	
	public void validateVersion(int version) {
		if (this.version != version) {
			throw new VersionMismatchException();
		}
	}

	public boolean validateClientIp(String clientIp) {
		if (this.clientIp != clientIp) {
			return false;
		}
		return true;
	}
	
    public void softDelete() {
    	this.deleted = true;
    	this.deletedAt = LocalDateTime.now();
    }

    @PrePersist
    private void onPrePersist() {
        String ip = CustomRequestContextHolder.getClientIp();

        if (ip != null) {
            this.clientIp = ip;
        } else {
            this.clientIp = "0.0.0.0"; // 혹은 "SYSTEM" 등
        }
    }
}
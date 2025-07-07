package org.project.resumeboost.basic.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 공용 클래스 설정
@EntityListeners(value = AuditingEntityListener.class)
public class BasicTime {
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createTime;

	@UpdateTimestamp
	@Column(insertable = false)
	private LocalDateTime updateTime;

}

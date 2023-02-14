package numble.bankingapi.member.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Member {
	@EqualsAndHashCode.Include
	private Long userId;
	private String email;
	private String name;
	private String password;
	private LocalDateTime createdDate;

	@Builder
	public Member(String email, String name, String password, LocalDateTime createdDate) {
		requiredNotNullAndNotBlank(email);
		requiredNotNullAndNotBlank(name);
		requiredNotNullAndNotBlank(password);
		this.email = email;
		this.name = name;
		this.password = password;
		this.createdDate = createdDate == null ? LocalDateTime.now() : createdDate;
	}

	private void requiredNotNullAndNotBlank(String field) {
		if (Objects.isNull(field) || field.isBlank()) {
			throw new NullPointerException();
		}
	}
}

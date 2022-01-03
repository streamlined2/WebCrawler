package org.training.campus.webcrawler.dao.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.NaturalId;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" })
@ToString
@Entity
@Table(name = "research", schema = "crawler")
public class Research implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.PROTECTED)
	private long id;

	@NaturalId
	private @NonNull LocalDateTime time;

	@NaturalId
	private @NonNull String startPage;

	@Column(name = "mufetched")
	private long maxUriFetched;

	@Column(name = "flcount")
	private long fetchedLinkCount;

	@Column(name = "maxdist")
	private long maxDistance;

	@Column(name = "elcount")
	private long externalLinksCount;

	@Column(name = "telapse")
	private long elapsedTime;

	@OneToMany(mappedBy = "research")
	private final Set<Page> pages = new HashSet<>();

}

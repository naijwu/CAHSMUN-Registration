package org.cahsmun.registration.sponsor;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface SponsorRepository extends PagingAndSortingRepository<Sponsor, Long> {
    Sponsor findByEmail(String email);
}

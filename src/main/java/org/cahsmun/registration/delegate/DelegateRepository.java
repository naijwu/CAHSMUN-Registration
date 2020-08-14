package org.cahsmun.registration.delegate;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DelegateRepository extends PagingAndSortingRepository<Delegate, Long> {

    List<Delegate> findByDelegationId(long delegationId);

    Delegate findByEmail(String email);
}

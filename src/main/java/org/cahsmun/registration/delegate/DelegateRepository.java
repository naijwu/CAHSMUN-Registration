package org.cahsmun.registration.delegate;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DelegateRepository extends PagingAndSortingRepository<Delegate, Long> {

    List<Delegate> findByDelegation_id(long delegation_id);
}

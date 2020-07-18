package org.cahsmun.registration.assignment;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AssignmentRepository extends PagingAndSortingRepository<Assignment, Long> {

}

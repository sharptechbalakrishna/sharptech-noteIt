
package com.sharp.noteIt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import com.sharp.noteIt.model.BorrowerDoc;

@Repository
@EnableJpaRepositories
public interface BorrowerRepository extends JpaRepository<BorrowerDoc, Long>{

}

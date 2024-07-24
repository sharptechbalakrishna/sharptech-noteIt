
package com.sharp.noteIt.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sharp.noteIt.model.BorrowerDoc;

public interface BorrowerRepository extends JpaRepository<BorrowerDoc, Long>{

}

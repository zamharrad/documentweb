package com.rad.document.repos;

import com.rad.document.enttiy.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends CrudRepository<Document,Long > {
}

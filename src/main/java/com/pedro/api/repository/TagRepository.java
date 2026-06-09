package com.pedro.api.repository;

import com.pedro.api.model.Tag; // Importação corrigida para a pasta model
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    // Busca uma etiqueta pelo nome exato (usado para evitar duplicidade)
    Optional<Tag> findByName(String name);
}
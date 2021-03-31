package br.com.systemsgs.repository;

import br.com.systemsgs.model.ModelLivros;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<ModelLivros, Long> {

    boolean existsByIsbn (String isbn);

}

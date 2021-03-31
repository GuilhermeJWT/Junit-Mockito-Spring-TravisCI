package br.com.systemsgs.repository;

import br.com.systemsgs.model.ModelLivros;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LivroRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    LivroRepository repository;

    @Test
    @DisplayName("Vai retornar verdadeiro se existir isbn existene no banco de dados")
    public void retornaVerdadeiroIsbnExistente(){

        String isbn = "123";

        ModelLivros modelLivros = ModelLivros.builder().titulo("Livro Java").autor("Guilherme").isbn(isbn).build();

        entityManager.persist(modelLivros);

        boolean existente = repository.existsByIsbn(isbn);

        assertThat(existente).isTrue();

    }

    @Test
    @DisplayName("Vai retornar falso se existir isbn existene no banco de dados")
    public void retornaFalsoIsbnInexistente(){

        String isbn = "123";

        boolean existente = repository.existsByIsbn(isbn);

        assertThat(existente).isFalse();

    }

}

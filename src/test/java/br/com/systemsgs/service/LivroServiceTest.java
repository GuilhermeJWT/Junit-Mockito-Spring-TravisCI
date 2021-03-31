package br.com.systemsgs.service;

import br.com.systemsgs.exception.RegraNegocioException;
import br.com.systemsgs.implementacao.LivroServiceImpl;
import br.com.systemsgs.model.ModelLivros;
import br.com.systemsgs.repository.LivroRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LivroServiceTest {

    LivroService service;

    @MockBean
    LivroRepository repository;

    @BeforeEach
    public void init(){

        this.service = new LivroServiceImpl(repository);

    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void salvarLivroTest(){

        ModelLivros modelLivros = criarLivro();

        Mockito.when(repository.save(modelLivros)).thenReturn(ModelLivros.builder().id(1L).isbn("123").autor("Guilherme Autor").titulo("Livro de Programação").build());

        ModelLivros dadosLivro = service.save(modelLivros);

        assertThat(dadosLivro.getId()).isNotNull();
        assertThat(dadosLivro.getIsbn()).isEqualTo("123");
        assertThat(dadosLivro.getTitulo()).isEqualTo("Livro de Programação");
        assertThat(dadosLivro.getAutor()).isEqualTo("Guilherme Autor");

    }

    @Test
    @DisplayName("Deve lançar Erro se o usuario informar Isbn duplicado")
    public void naoSalvarLivroIsbnDuplicado(){

        ModelLivros modelLivros = criarLivro();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

       Throwable exception =  Assertions.catchThrowable(() -> service.save(modelLivros));
       assertThat(exception)
               .isInstanceOf(RegraNegocioException.class)
               .hasMessage("Isbn já Cadastrado!");

       Mockito.verify(repository, Mockito.never()).save(modelLivros);

    }

    public ModelLivros criarLivro(){
        return ModelLivros.builder().isbn("123").autor("Guilherme Autor").titulo("Livro de Programação").build();
    }

}

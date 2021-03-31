package br.com.systemsgs.test;

import br.com.systemsgs.dto.LivroDTO;
import br.com.systemsgs.exception.RegraNegocioException;
import br.com.systemsgs.model.ModelLivros;
import br.com.systemsgs.service.LivroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class LivroControllerTest {

    static String API_LIVROS = "/api/livros";

    @Autowired
    MockMvc mvc;

    @MockBean
    LivroService service;

    @Test
    @DisplayName("Esse Controlador deve salvar um livro")
    public void salvarLivrosTest() throws Exception{

        LivroDTO livroDTO = criarLivro();

        ModelLivros dadosLivros = ModelLivros.builder().id(1L).autor("Guilherme Santos").titulo("Um Livro Legal").isbn("123").build();

        BDDMockito.given(service.save(Mockito.any(ModelLivros.class))).willReturn(dadosLivros);

        String jsonTeste = new ObjectMapper().writeValueAsString(livroDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API_LIVROS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonTeste);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("titulo").value(livroDTO.getTitulo()))
                .andExpect(jsonPath("autor").value(livroDTO.getAutor()))
                .andExpect(jsonPath("isbn").value(livroDTO.getIsbn()));
    }

    @Test
    @DisplayName("Vai retornar o erro se o usuario não informar os campos obrigatórios")
    public void validandoCamposLivroTest() throws Exception{

        String json = new ObjectMapper().writeValueAsString(new LivroDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API_LIVROS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("erros", hasSize(3)));

    }

    @Test
    @DisplayName("Lançar Erro se tiver Isbn duplicados")
    public void validarIsbnDuplicadoTest() throws Exception{

        LivroDTO dto = criarLivro();

        String msgErro = "Isbn já cadastrado, Por Favor informe outro!";

        String json = new ObjectMapper().writeValueAsString(new LivroDTO());
        BDDMockito.given(service.save(Mockito.any(ModelLivros.class)))
                .willThrow(new RegraNegocioException(msgErro));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API_LIVROS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("erros", hasSize(1)))
                .andExpect(jsonPath("erros[0]").value(msgErro));

     }

    public LivroDTO criarLivro(){
        return LivroDTO.builder().id(1L).autor("Guilherme Santos").titulo("Um Livro Legal").isbn("123").build();
    }

}

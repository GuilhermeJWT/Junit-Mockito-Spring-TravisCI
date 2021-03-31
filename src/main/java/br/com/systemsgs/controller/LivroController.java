package br.com.systemsgs.controller;

import br.com.systemsgs.dto.LivroDTO;
import br.com.systemsgs.exception.RegraNegocioException;
import br.com.systemsgs.model.ModelLivros;
import br.com.systemsgs.service.LivroService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import br.com.systemsgs.exception.ValidaCamposException;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/livros")
public class LivroController {

    private LivroService livroService;

    private ModelMapper modelMapper;

    public LivroController(LivroService livroService, ModelMapper mapper) {
        this.livroService = livroService;
        this.modelMapper =mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivroDTO salvarLivro(@RequestBody @Valid LivroDTO livroDTO){

        ModelLivros dados = modelMapper.map(livroDTO, ModelLivros.class);

        dados = livroService.save(dados);

        return modelMapper.map(dados, LivroDTO.class);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidaCamposException validaCamposException(MethodArgumentNotValidException exception){

        BindingResult bindingResult = exception.getBindingResult();

        return new ValidaCamposException(bindingResult);

    }

    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidaCamposException validarRegraNegocioException(RegraNegocioException regra){

         return new ValidaCamposException(regra);

    }

}

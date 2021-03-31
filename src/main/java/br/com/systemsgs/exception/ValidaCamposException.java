package br.com.systemsgs.exception;

import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValidaCamposException {

    private List<String> erros;

    public ValidaCamposException(BindingResult bindingResult){
        this.erros = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error -> this.erros.add(error.getDefaultMessage()));
    }

    public List<String> getErros() {
        return erros;
    }

    public ValidaCamposException(RegraNegocioException regra){

        this.erros = Arrays.asList(regra.getMessage());

    }
}

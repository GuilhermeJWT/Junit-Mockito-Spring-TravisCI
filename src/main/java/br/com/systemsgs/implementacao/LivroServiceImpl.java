package br.com.systemsgs.implementacao;

import br.com.systemsgs.exception.RegraNegocioException;
import br.com.systemsgs.model.ModelLivros;
import br.com.systemsgs.repository.LivroRepository;
import br.com.systemsgs.service.LivroService;
import org.springframework.stereotype.Service;

@Service
public class LivroServiceImpl implements LivroService {

    private LivroRepository livroRepository;

    public LivroServiceImpl(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Override
    public ModelLivros save(ModelLivros modelLivros) {
        if (livroRepository.existsByIsbn(modelLivros.getIsbn())){
            throw new RegraNegocioException("Isbn Já Cadastrado");
        }
        return livroRepository.save(modelLivros);
    }
}

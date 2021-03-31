package br.com.systemsgs.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LivroDTO implements Serializable {

    private Long id;

    @NotEmpty
    private String titulo;

    @NotEmpty
    private String autor;

    @NotEmpty
    private String isbn;

}

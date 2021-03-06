package com.es.agriculturafamiliar.entity.produtor;

import com.es.agriculturafamiliar.entity.Endereco;
import com.es.agriculturafamiliar.entity.User;
import com.es.agriculturafamiliar.entity.Produto;
import com.es.agriculturafamiliar.enums.TipoProdutor;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class Produtor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(unique = true)
    private String cpfOuCnpj;

    @NotBlank
    private String nome;
    @NotBlank
    private String nomeFantasia;


    @OneToMany(mappedBy = "produtor", cascade = CascadeType.ALL)
    private List<Endereco> enderecos;
    private String regiaoDeProducao; //geolocalização?
    private Boolean atendeNoEnderecoDeProducao = false;
    private Boolean cadastroEntidade = false;

    @NotNull
    private Integer tipoProdutor = TipoProdutor.INDIVIDUAL.getCod();
    private Boolean registroOuCertificacao = false;
    private Boolean agroecologico = false;
    private Boolean certificacaoAgroecologico = false;
    private String organico = "SIM";
    private String geolocalizacao;

    @OneToMany(mappedBy = "produtor")
    private List<Produto> produtos = new ArrayList<>();

    @ManyToMany
    @JoinTable(name="PRODUTOR_TIPO_PRODUCAO",
                joinColumns = @JoinColumn(name="produtor_id"),
                inverseJoinColumns = @JoinColumn(name="tipo_producao_id"))
    private Set<TipoProducao> tiposProducao = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "TELEFONE")
    private Set<String> telefones = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "ENTIDADE_ATENDIDA")
    private Set<String> entidadesAtendidas = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "FORMA_PAGAMENTO")
    private Set<String> formasPagamento = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "REGISTROS_CERTIFICADOS")
    private Set<String> registrosOuCertificacoes = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "PAGINAS_EXTERNAS")
    private Set<String> paginasExternas = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Produtor(String cpfOuCnpj, String nome, String nomeFantasia,
                    String regiaoDeProducao,
                    Boolean atendeNoEnderecoDeProducao,
                    Boolean cadastroEntidade, TipoProdutor tipoProdutor, Boolean registroOuCertificacao,
                    Boolean agroecologico, Boolean certificacaoAgroecologico,
                    String organico, String geolocalizacao) {
        this.cpfOuCnpj = cpfOuCnpj;
        this.nome = nome;
        this.nomeFantasia = nomeFantasia;
        this.regiaoDeProducao = regiaoDeProducao;
        this.atendeNoEnderecoDeProducao = atendeNoEnderecoDeProducao;
        this.cadastroEntidade = cadastroEntidade;
        this.tipoProdutor = tipoProdutor.getCod();
        this.registroOuCertificacao = registroOuCertificacao;
        this.agroecologico = agroecologico;
        this.certificacaoAgroecologico = certificacaoAgroecologico;
        this.organico = organico;
        this.geolocalizacao = geolocalizacao;
    }

    @PrePersist
	private void prePersist() {        
	    if(enderecos != null) {
	    	enderecos.forEach(e -> e.setProdutor(this));
	    };
	}

    public void setTipoProdutor(TipoProdutor tipo) {this.tipoProdutor = tipo.getCod();}
    public TipoProdutor getTipoProdutor() {return TipoProdutor.toEnum(this.tipoProdutor);}
}

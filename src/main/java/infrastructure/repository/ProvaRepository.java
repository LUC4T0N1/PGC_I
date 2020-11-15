package infrastructure.repository;
import domain.Prova;
import domain.Questao;
import infrastructure.dto.BuscaPaginadaDto;
import infrastructure.dto.BuscarProvasDto;
import infrastructure.dto.ProvaDto;
import infrastructure.dto.QuestaoDto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProvaRepository  implements PanacheRepository<Prova> {
    public static final int TAMANHO_PAGINA = 10;
    @Inject
    EntityManager em;

    public Prova buscarPorId(Long id) {
        Prova prova = findById(id);
        if (prova == null) throw new WebApplicationException("Prova não encontrada",Response.Status.NOT_FOUND);
        return prova;
    }

    public ProvaDto buscarProvaInteira(Long id) {
        Prova prova = findById(id);
        if (prova == null) throw new WebApplicationException("Prova não encontrada",Response.Status.NOT_FOUND);
        ProvaDto provaDto = ProvaDto.instanciar(prova);
        List<QuestaoDto> questoes = new ArrayList<>();
        for(Questao questao : prova.getQuestoes()){
            questoes.add(QuestaoDto.instanciar(questao));
        }
        provaDto.setQuestoes(questoes);
        return provaDto;
    }

    public void cadastrarProva(Prova prova){
        try {
            persist(prova);
        } catch (WebApplicationException e) {
            throw new WebApplicationException(e.getMessage(), e.getResponse());
        }
    }

    public BuscaPaginadaDto buscarPorUsuario(Integer pagina, String usuario) {
        try {
            List<Prova> conteudos =  find("usuario = ?1", Sort.by("nome").descending(), usuario)
                    .page(Page.of(pagina, TAMANHO_PAGINA))
                    .list();
            List<ProvaDto> provasDto = conteudos.stream()
                    .map(
                            ProvaDto::instanciar)
                    .collect(Collectors.toList());

            return BuscaPaginadaDto.instanciar( null, provasDto);

        } catch (Exception e) {
            throw new                     WebApplicationException(
                    "Erro ao obter produtos", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


    public BuscaPaginadaDto buscarPorOrdemAlfabetica(Integer pagina, BuscarProvasDto dto) {
        try {
            Query query = this.em.createNativeQuery("SELECT provas_id FROM conteudo_prova WHERE conteudo_id = :idConteudo");
            query.setParameter("idConteudo", dto.idConteudo);
            List vetor = query.getResultList();
            List<Long> ids = new ArrayList<>();
            for (Object id : vetor) {
                ids.add(Long.valueOf(String.valueOf(id)));
            }
            String filtro = "";
            if(dto.ordemAlfabetica && !dto.dificuldade && !dto.popularidade) filtro = "nome";
            else if (!dto.ordemAlfabetica && dto.dificuldade && !dto.popularidade) filtro = "dificuldade";
            else if(!dto.ordemAlfabetica && !dto.dificuldade && dto.popularidade) filtro = "realizacoes";
            else throw new WebApplicationException("Requisição errada!", Response.Status.BAD_REQUEST);
            System.out.println(filtro);
            List<Prova> conteudos =  find("publica = 1 AND id IN ?1", Sort.by(filtro).descending(), ids)
                    .page(Page.of(pagina, TAMANHO_PAGINA))
                    .list();
            List<ProvaDto> provasDto = conteudos.stream()
                    .map(
                            ProvaDto::instanciar)
                    .collect(Collectors.toList());
            return BuscaPaginadaDto.instanciar( null, provasDto);
        } catch (WebApplicationException e) {
            throw new WebApplicationException(e.getMessage(), e.getResponse());
        }
    }

}

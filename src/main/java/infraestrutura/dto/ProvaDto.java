package infraestrutura.dto;

import aplicacao.utlis.DataUtils;
import dominio.Prova;
import dominio.Questao;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Schema(
        name = "Provas",
        description = "JSON com informações sobre uma prova específica")

public class ProvaDto {
    public String nome;
    public BigDecimal notaMaxima;
    public List<Long> idsQuestoes;
    public Long id;
    public Boolean publica;
    public BigDecimal mediaNotas;
    public Long popularidade;
    public List<QuestaoDto> questoes;
    public BigDecimal tempo;
    public String dataInicial;
    public String dataFinal;
    public Long tentativas;
    public int quantidadeQuestoes;

    public Prova paraDominio(ProvaDto dto, List<Questao> questoes, String usuario, Boolean publica){
        return Prova.instanciar(dto, questoes, usuario, publica);
    }

    public static ProvaDto instanciar(Prova prova){
        ProvaDto provaDto = new ProvaDto();
        provaDto.setNome(prova.getNome());
        provaDto.setId(prova.getId());
        provaDto.setMediaNotas(prova.getMediaNotas());
        provaDto.setPopularidade(prova.getRealizacoes());
        provaDto.setDataFinal(DataUtils.converterParaString(prova.getDataFinal()));
        provaDto.setDataInicial(DataUtils.converterParaString(prova.getDataInicial()));
        provaDto.setQuantidadeQuestoes(prova.getQuantidadeQuestoes());
        provaDto.setPublica(prova.getPublica());
        provaDto.setTentativas(prova.getTentativas());
        provaDto.setNotaMaxima(prova.getNotaMaxima());
        List<QuestaoDto> questaoDtos = new ArrayList<>();
        for(Questao questao : prova.getQuestoes()){
            questaoDtos.add(QuestaoDto.instanciar(questao));
        }
        provaDto.setQuestoes(questaoDtos);
        return provaDto;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setNotaMaxima(BigDecimal notaMaxima) {
        this.notaMaxima = notaMaxima;
    }

    public void setIdsQuestoes(List<Long> idsQuestoes) {
        this.idsQuestoes = idsQuestoes;
    }

    public void setPublica(Boolean publica) {
        this.publica = publica;
    }

    public void setTempo(BigDecimal tempo) {
        this.tempo = tempo;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public void setTentativas(Long tentativas) {
        this.tentativas = tentativas;
    }

    public void setQuantidadeQuestoes(int quantidadeQuestoes) {
        this.quantidadeQuestoes = quantidadeQuestoes;
    }

    public void setMediaNotas(BigDecimal mediaNotas) {
        this.mediaNotas = mediaNotas;
    }

    public Long getPopularidade() {
        return popularidade;
    }

    public void setPopularidade(Long popularidade) {
        this.popularidade = popularidade;
    }

    public void setQuestoes(List<QuestaoDto> questoes) {
        this.questoes = questoes;
    }
}

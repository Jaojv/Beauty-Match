package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.dto.PerguntaDTO;
import com.beauty.com.MatchBeauty.dto.RecomendacaoDTO;
import com.beauty.com.MatchBeauty.dto.RespostaQuizDTO;
import com.beauty.com.MatchBeauty.entity.*;
import com.beauty.com.MatchBeauty.exception.QuizException;
import com.beauty.com.MatchBeauty.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuizService {
    
    @Autowired
    private PerguntaRepository perguntaRepository;
    
    @Autowired
    private AlternativaRepository alternativaRepository;
    
    @Autowired
    private RecomendacaoRepository recomendacaoRepository;
    
    @Autowired
    private RespostaQuizRepository respostaQuizRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    /**
     * Lista todas as perguntas ativas ordenadas por ordem
     */
    public List<PerguntaDTO> listarPerguntas() {
        List<Pergunta> perguntas = perguntaRepository.findAllAtivasOrderByOrdem();
        return perguntas.stream()
                .map(PerguntaDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Salva ou atualiza as respostas do cliente e retorna a recomendação
     */
    public RecomendacaoDTO salvarRespostas(RespostaQuizDTO respostaQuizDTO) {
        // Validar se o cliente existe
        Cliente cliente = clienteRepository.findById(respostaQuizDTO.getClienteId())
                .orElseThrow(() -> new QuizException("Cliente não encontrado"));
        
        // Validar respostas
        validarRespostas(respostaQuizDTO.getRespostas());
        
        // Gerar critério baseado nas respostas
        String criterio = gerarCriterio(respostaQuizDTO.getRespostas());
        
        // Buscar recomendação
        Recomendacao recomendacao = recomendacaoRepository.findByCriterioAndAtivoTrue(criterio)
                .orElseThrow(() -> new QuizException("Recomendação não encontrada para o critério: " + criterio));
        
        // Salvar ou atualizar resposta do quiz
        Optional<RespostaQuiz> respostaExistente = respostaQuizRepository.findByCliente_IdUsuario(respostaQuizDTO.getClienteId());
        
        RespostaQuiz respostaQuiz;
        if (respostaExistente.isPresent()) {
            // Atualizar resposta existente
            respostaQuiz = respostaExistente.get();
            respostaQuiz.setRespostas(respostaQuizDTO.getRespostas());
            respostaQuiz.setCriterioGerado(criterio);
        } else {
            // Criar nova resposta
            respostaQuiz = new RespostaQuiz();
            respostaQuiz.setCliente(cliente);
            respostaQuiz.setRespostas(respostaQuizDTO.getRespostas());
            respostaQuiz.setCriterioGerado(criterio);
        }
        
        respostaQuizRepository.save(respostaQuiz);
        
        return RecomendacaoDTO.fromEntity(recomendacao);
    }
    
    /**
     * Valida se todas as perguntas foram respondidas
     */
    private void validarRespostas(Map<String, String> respostas) {
        if (respostas == null || respostas.isEmpty()) {
            throw new QuizException("As respostas são obrigatórias");
        }
        
        List<Pergunta> perguntas = perguntaRepository.findAllAtivasOrderByOrdem();
        
        for (Pergunta pergunta : perguntas) {
            String resposta = respostas.get(pergunta.getTexto());
            if (resposta == null || resposta.trim().isEmpty()) {
                throw new QuizException("Pergunta obrigatória não respondida: " + pergunta.getTexto());
            }
            
            // Validar se a resposta é uma alternativa válida
            boolean alternativaValida = pergunta.getAlternativas().stream()
                    .anyMatch(alt -> alt.getTexto().equals(resposta) && alt.getAtivo());
            
            if (!alternativaValida) {
                throw new QuizException("Resposta inválida para a pergunta: " + pergunta.getTexto());
            }
        }
    }
    
    /**
     * Gera critério baseado nas respostas do cliente
     */
    private String gerarCriterio(Map<String, String> respostas) {
        // Ordenar as respostas por ordem das perguntas para garantir consistência
        List<Pergunta> perguntas = perguntaRepository.findAllAtivasOrderByOrdem();
        
        StringBuilder criterio = new StringBuilder();
        
        for (Pergunta pergunta : perguntas) {
            String resposta = respostas.get(pergunta.getTexto());
            if (resposta != null) {
                if (criterio.length() > 0) {
                    criterio.append("_");
                }
                criterio.append(resposta.toUpperCase().replace(" ", ""));
            }
        }
        
        return criterio.toString();
    }
    
    /**
     * Busca recomendação por critério
     */
    public RecomendacaoDTO buscarRecomendacaoPorCriterio(String criterio) {
        Recomendacao recomendacao = recomendacaoRepository.findByCriterioAndAtivoTrue(criterio)
                .orElseThrow(() -> new QuizException("Recomendação não encontrada para o critério: " + criterio));
        
        return RecomendacaoDTO.fromEntity(recomendacao);
    }
    
    /**
     * Lista todas as recomendações ativas
     */
    public List<RecomendacaoDTO> listarRecomendacoes() {
        List<Recomendacao> recomendacoes = recomendacaoRepository.findAllAtivasOrderByCriterio();
        return recomendacoes.stream()
                .map(RecomendacaoDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca resposta do quiz por cliente
     */
    public RespostaQuizDTO buscarRespostaPorCliente(Long clienteId) {
        RespostaQuiz respostaQuiz = respostaQuizRepository.findByCliente_IdUsuario(clienteId)
                .orElse(null);
        
        return respostaQuiz != null ? RespostaQuizDTO.fromEntity(respostaQuiz) : null;
    }
    
    /**
     * Verifica se o cliente já respondeu o quiz
     */
    public boolean clienteJaRespondeu(Long clienteId) {
        return respostaQuizRepository.existsByCliente_IdUsuario(clienteId);
    }
} 
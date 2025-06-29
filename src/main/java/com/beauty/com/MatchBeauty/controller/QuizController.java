package com.beauty.com.MatchBeauty.controller;

import com.beauty.com.MatchBeauty.dto.PerguntaDTO;
import com.beauty.com.MatchBeauty.dto.RecomendacaoDTO;
import com.beauty.com.MatchBeauty.dto.RespostaQuizDTO;
import com.beauty.com.MatchBeauty.exception.QuizException;
import com.beauty.com.MatchBeauty.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
@Tag(name = "Quiz", description = "Endpoints para gestão do quiz de recomendações")
public class QuizController {
    
    @Autowired
    private QuizService quizService;
    
    /**
     * Lista todas as perguntas ativas com suas alternativas
     */
    @GetMapping("/perguntas")
    @Operation(summary = "Listar perguntas", description = "Retorna todas as perguntas ativas ordenadas por ordem")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perguntas listadas com sucesso",
                    content = @Content(schema = @Schema(implementation = PerguntaDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<PerguntaDTO>> listarPerguntas() {
        try {
            List<PerguntaDTO> perguntas = quizService.listarPerguntas();
            return ResponseEntity.ok(perguntas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Salva as respostas do cliente e retorna a recomendação
     */
    @PostMapping("/responder")
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Responder quiz", description = "Salva as respostas do cliente e retorna a recomendação personalizada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Respostas salvas e recomendação retornada",
                    content = @Content(schema = @Schema(implementation = RecomendacaoDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou respostas incompletas"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<?> responderQuiz(
            @Parameter(description = "Dados das respostas do quiz", required = true)
            @Valid @RequestBody RespostaQuizDTO respostaQuizDTO) {
        try {
            RecomendacaoDTO recomendacao = quizService.salvarRespostas(respostaQuizDTO);
            return ResponseEntity.ok(recomendacao);
        } catch (QuizException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Lista todas as recomendações cadastradas (apenas admin)
     */
    @GetMapping("/recomendacoes")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar recomendações", description = "Retorna todas as recomendações cadastradas (apenas administradores)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recomendações listadas com sucesso",
                    content = @Content(schema = @Schema(implementation = RecomendacaoDTO.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<RecomendacaoDTO>> listarRecomendacoes() {
        try {
            List<RecomendacaoDTO> recomendacoes = quizService.listarRecomendacoes();
            return ResponseEntity.ok(recomendacoes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Cadastra uma nova recomendação (apenas admin)
     */
    @PostMapping("/recomendacoes")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cadastrar recomendação", description = "Cadastra uma nova recomendação (apenas administradores)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Recomendação cadastrada com sucesso",
                    content = @Content(schema = @Schema(implementation = RecomendacaoDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<?> cadastrarRecomendacao(
            @Parameter(description = "Dados da recomendação", required = true)
            @Valid @RequestBody RecomendacaoDTO recomendacaoDTO) {
        try {
            // TODO: Implementar método no service para cadastrar recomendação
            return ResponseEntity.status(HttpStatus.CREATED).body(recomendacaoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Busca recomendação por critério específico
     */
    @GetMapping("/recomendacoes/{criterio}")
    @Operation(summary = "Buscar recomendação por critério", description = "Retorna a recomendação para um critério específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recomendação encontrada",
                    content = @Content(schema = @Schema(implementation = RecomendacaoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Recomendação não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<?> buscarRecomendacaoPorCriterio(
            @Parameter(description = "Critério da recomendação", required = true)
            @PathVariable String criterio) {
        try {
            RecomendacaoDTO recomendacao = quizService.buscarRecomendacaoPorCriterio(criterio);
            return ResponseEntity.ok(recomendacao);
        } catch (QuizException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Verifica se o cliente já respondeu o quiz
     */
    @GetMapping("/cliente/{clienteId}/status")
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Verificar status do quiz", description = "Verifica se o cliente já respondeu o quiz")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status verificado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Map<String, Object>> verificarStatusQuiz(
            @Parameter(description = "ID do cliente", required = true)
            @PathVariable Long clienteId) {
        try {
            boolean jaRespondeu = quizService.clienteJaRespondeu(clienteId);
            Map<String, Object> response = new HashMap<>();
            response.put("clienteId", clienteId);
            response.put("jaRespondeu", jaRespondeu);
            
            if (jaRespondeu) {
                RespostaQuizDTO resposta = quizService.buscarRespostaPorCliente(clienteId);
                response.put("resposta", resposta);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Busca resposta do quiz por cliente
     */
    @GetMapping("/cliente/{clienteId}/resposta")
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Buscar resposta do cliente", description = "Retorna a resposta do quiz de um cliente específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resposta encontrada",
                    content = @Content(schema = @Schema(implementation = RespostaQuizDTO.class))),
        @ApiResponse(responseCode = "404", description = "Resposta não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<?> buscarRespostaCliente(
            @Parameter(description = "ID do cliente", required = true)
            @PathVariable Long clienteId) {
        try {
            RespostaQuizDTO resposta = quizService.buscarRespostaPorCliente(clienteId);
            if (resposta != null) {
                return ResponseEntity.ok(resposta);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 
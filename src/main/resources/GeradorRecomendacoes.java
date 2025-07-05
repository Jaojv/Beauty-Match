import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GeradorRecomendacoes {
    
    public static void main(String[] args) {
        String[] tiposCabelo = {"LISO", "CACHEADO", "CRESPO", "ONDULADO"};
        String[] tonsPele = {"CLARA", "MEDIA", "ESCURA", "MUITOESCURA"};
        String[] formatosRosto = {"OVAL", "REDONDO", "QUADRADO", "DIAMANTE", "TRIANGULAR"};
        String[] estilos = {"CLASSICO", "MODERNO", "ROMANTICO", "AVENTUREIRO", "MINIMALISTA"};
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/todas_recomendacoes.sql"))) {
            writer.println("-- Script gerado automaticamente com todas as 400 combinações de critérios");
            writer.println("-- Total: 4 tipos de cabelo × 4 tons de pele × 5 formatos de rosto × 5 estilos = 400 combinações");
            writer.println();
            writer.println("INSERT IGNORE INTO recomendacoes (criterio, descricao, ativo, created_at, updated_at) VALUES");
            
            int contador = 0;
            for (String cabelo : tiposCabelo) {
                for (String pele : tonsPele) {
                    for (String rosto : formatosRosto) {
                        for (String estilo : estilos) {
                            contador++;
                            String criterio = cabelo + "_" + pele + "_" + rosto + "_" + estilo;
                            String descricao = gerarDescricao(cabelo, pele, rosto, estilo);
                            
                            writer.print("('" + criterio + "', '" + descricao + "', true, NOW(), NOW())");
                            
                            if (contador < 400) {
                                writer.println(",");
                            } else {
                                writer.println(";");
                            }
                        }
                    }
                }
            }
            
            writer.println();
            writer.println("-- Recomendação padrão para combinações não mapeadas");
            writer.println("INSERT IGNORE INTO recomendacoes (criterio, descricao, ativo, created_at, updated_at) VALUES");
            writer.println("('PADRAO', 'Com base nas suas características, recomendamos consultar um profissional para uma avaliação personalizada. Cada pessoa é única e merece um tratamento individualizado que valorize suas características naturais e atenda às suas preferências de estilo.', true, NOW(), NOW());");
            
            System.out.println("Script gerado com " + contador + " combinações!");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String gerarDescricao(String cabelo, String pele, String rosto, String estilo) {
        StringBuilder descricao = new StringBuilder();
        
        // Descrição baseada no tipo de cabelo
        switch (cabelo) {
            case "LISO":
                descricao.append("Para cabelos lisos");
                break;
            case "CACHEADO":
                descricao.append("Para cabelos cacheados");
                break;
            case "CRESPO":
                descricao.append("Para cabelos crespos");
                break;
            case "ONDULADO":
                descricao.append("Para cabelos ondulados");
                break;
        }
        
        descricao.append(" com pele ").append(pele.toLowerCase()).append(" e rosto ").append(rosto.toLowerCase());
        
        // Adicionar recomendação baseada no estilo
        switch (estilo) {
            case "CLASSICO":
                descricao.append(", recomendamos um corte clássico em camadas médias com franja lateral. Este estilo valoriza a simetria natural e cria um visual elegante e atemporal.");
                break;
            case "MODERNO":
                descricao.append(", sugerimos um corte moderno com assimetria e textura. Este estilo adiciona personalidade e contemporaneidade ao seu visual.");
                break;
            case "ROMANTICO":
                descricao.append(", recomendamos um corte em camadas longas com volume lateral. Este estilo cria um visual romântico e feminino.");
                break;
            case "AVENTUREIRO":
                descricao.append(", sugerimos um corte pixie moderno ou bob assimétrico. Este estilo adiciona personalidade e expressividade ao seu visual.");
                break;
            case "MINIMALISTA":
                descricao.append(", recomendamos um corte bob médio limpo e simétrico. Este estilo mantém a simplicidade e elegância.");
                break;
        }
        
        // Adicionar sugestões de cor baseadas na pele
        descricao.append(" Tons de cor recomendados: ");
        switch (pele) {
            case "CLARA":
                descricao.append("castanho claro, loiro dourado ou caramelo suave.");
                break;
            case "MEDIA":
                descricao.append("castanho médio, caramelo ou castanho dourado.");
                break;
            case "ESCURA":
                descricao.append("castanho escuro, preto profundo ou caramelo escuro.");
                break;
            case "MUITOESCURA":
                descricao.append("preto profundo, castanho muito escuro ou preto azulado.");
                break;
        }
        
        return descricao.toString();
    }
} 
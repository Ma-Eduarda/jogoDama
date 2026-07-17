package com.damas.objetos;

import java.util.ArrayList;

/**
 * Armazena o tabuleiro e responsavel por posicionar as pecas.
 * 
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 * @author João Victor da S. Cirilo {@link joao.cirilo@academico.ufpb.br}
 * @author José Alisson Rocha da Silva {@link jose.alisson2@academico.ufpb.br}
 */

public class Jogo {

    private Tabuleiro tabuleiro;
    private Jogador jogadorUm;
    private Jogador jogadorDois;
    private int vezAtual = 1;
    private int jogadas = 0;
    private int jogadasSemComerPeca = 0;
    private ArrayList<Casa> pecasAComer;
    private Casa casaBloqueadaOrigem;
    
    // Especialista em validação de movimentos
    private ValidarMovimentos validador;

    public Jogo() {
        tabuleiro = new Tabuleiro();
        pecasAComer = new ArrayList<>();
        jogadorUm = new Jogador("player branco");
        jogadorDois = new Jogador("player vermelho");
        
        vezAtual = 1;
        jogadas = 0;
        jogadasSemComerPeca = 0;
        casaBloqueadaOrigem = null;

        this.validador = new ValidarMovimentos(tabuleiro);
        ColocarPecas.colocarPecas(tabuleiro);
    }
    
    /**
     * Move uma peça no tabuleiro, validando o movimento e aplicando as regras do jogo
     */
    public void moverPeca(int origemX, int origemY, int destinoX, int destinoY) {
        Casa origem = tabuleiro.getCasa(origemX, origemY);
        Casa destino = tabuleiro.getCasa(destinoX, destinoY);
        Pedra peca = origem.getPeca();

        if (peca == null) return;

        if (casaBloqueadaOrigem == null) {
            if (isTurnoDoJogadorCorreto(peca)) {
                if (peca.isMovimentoValido(destino)) {
                    
                    if (validador.simularMovimentoEValidar(origem, destino, pecasAComer)) {                    
                        peca.mover(destino);

                        if (pecasAComer.size() > 0) {
                            comerPecas();
                            if (validador.deveContinuarJogando(destino)) {
                                casaBloqueadaOrigem = destino;
                            } else {
                                trocarDeVez();
                            }
                        } else {
                            jogadasSemComerPeca++;
                            trocarDeVez();
                        }
                        jogadas++;
                        if (validador.podeTransformarParaDama(destino)) {
                            transformarPedraParaDama(destino);
                        }
                    }
                }
            }
        } else {
            if (origem.equals(casaBloqueadaOrigem)) {
                if (validador.simularMovimentoEValidar(origem, destino, pecasAComer)) {
                    if (pecasAComer.size() != 0) {
                        casaBloqueadaOrigem = null;
                        moverPeca(origemX, origemY, destinoX, destinoY);
                    }
                }
            }
        }
    }

    /**
     * Verifica se é o turno correto do jogador
     */
    private boolean isTurnoDoJogadorCorreto(Pedra peca) {
        int tipo = peca.getTipo();
        if (vezAtual == 1) {
            return tipo == Pedra.PEDRA_BRANCA || tipo == Pedra.DAMA_BRANCA;
        }
        if (vezAtual == 2) {
            return tipo == Pedra.PEDRA_VERMELHA || tipo == Pedra.DAMA_VERMELHA;
        }
        return false;
    }

    /**
     * Remove peças capturadas e atualiza pontos
     */
    private void comerPecas() {
        int pecasComidas = pecasAComer.size();

        if (vezAtual == 1) jogadorUm.addPonto(pecasComidas);
        if (vezAtual == 2) jogadorDois.addPonto(pecasComidas);

        for (Casa casa : pecasAComer) {
            casa.removerPeca();
        }

        pecasAComer.clear();
        jogadasSemComerPeca = 0;
    }

    /**
     * Transforma uma pedra em dama
     */
    private void transformarPedraParaDama(Casa casa) {
        Pedra pedra = casa.getPeca();

        if (pedra.getTipo() == Peca.PEDRA_BRANCA) {
            casa.colocarPeca(new Dama(casa, Peca.DAMA_BRANCA));
        } else {
            casa.colocarPeca(new Dama(casa, Peca.DAMA_VERMELHA));
        }
    }

    public void trocarDeVez() {
        vezAtual = (vezAtual == 1) ? 2 : 1;
    }

    public int getGanhador() {
        if (jogadorUm.getPontos() == 12) return 1;
        if (jogadorDois.getPontos() == 12) return 2;
        return 0;
    }

    public Tabuleiro getTabuleiro() { return tabuleiro; }
    public void setJogadorUm(Jogador jogador) { jogadorUm = jogador; }
    public void setJogadorDois(Jogador jogador) { jogadorDois = jogador; }
    public Jogador getJogadorUm() { return jogadorUm; }
    public Jogador getJogadorDois() { return jogadorDois; }
    public int getVez() { return vezAtual; }
    public int getJogadasSemComerPecas() { return jogadasSemComerPeca; }
    public int getJogada() { return jogadas; }
    public Casa getCasaBloqueada() { return casaBloqueadaOrigem; }

    @Override
    public String toString() {
        String retorno = "Vez: " + (getVez() == 1 ? jogadorUm.getNome() : jogadorDois.getNome()) + "\n";
        retorno += "Nº de jogadas: " + getJogada() + "\n";
        retorno += "Jogadas sem comer peça: " + getJogadasSemComerPecas() + "\n\n";
        retorno += "Informações do(a) jogador(a) " + jogadorUm.getNome() + "\n";
        retorno += "Pontos: " + jogadorUm.getPontos() + "\n";
        retorno += "Nº de peças restantes: " + (12 - jogadorDois.getPontos()) + "\n\n";        
        retorno += "Informações do(a) jogador(a) " + jogadorDois.getNome() + "\n";
        retorno += "Pontos: " + jogadorDois.getPontos() + "\n";
        retorno += "Nº de peças restantes: " + (12 - jogadorUm.getPontos()) + "\n";

        if (casaBloqueadaOrigem != null) {
            retorno += "\nMova a peça na casa " + casaBloqueadaOrigem.getX() + ":" + casaBloqueadaOrigem.getY() + "!";
        }

        return retorno;
    }
}
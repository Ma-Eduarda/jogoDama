package com.damas.objetos;

import java.util.ArrayList;

public class ValidarMovimentos {
    private Tabuleiro tabuleiro;

    public ValidarMovimentos(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    public boolean simularMovimentoEValidar(Casa origem, Casa destino, ArrayList<Casa> pecasAComer) {
        Peca peca = origem.getPeca();
        int pecasConsecutivas = 0;

        if (destino.getPeca() != null) return false;

        int sentidoX = (destino.getX() - origem.getX());
        int sentidoY = (destino.getY() - origem.getY());
        int distanciaX = Math.abs(sentidoX); 
        int distanciaY = Math.abs(sentidoY);
        
        if ((distanciaX == 0) || (distanciaY == 0)) return false;

        sentidoX = sentidoX/distanciaX;
        sentidoY = sentidoY/distanciaY;

        // REGRA PARA A DISTÂNCIA DE 2 BLOCOS
        if ((distanciaX == 2 && distanciaY == 2) && !peca.isDama()) {
            Casa casa = tabuleiro.getCasa((destino.getX() - sentidoX), (destino.getY() - sentidoY));
            if (casa.getPeca() == null) return false;
        } else {
            // REGRA PARA A DISTÂNCIA DE 1 BLOCO
            if (!peca.isDama()) {
                if ((distanciaX == 1 || distanciaY == 1) && (distanciaX == distanciaY)) {
                    return peca.isDirecaoValida(sentidoY);
                } else {
                    return false;
                }
            }
        }

        int i = origem.getX();
        int j = origem.getY();

        while (!((i == destino.getX()) || (j == destino.getY()))) {
            i += sentidoX;
            j += sentidoY;

            Casa alvo = tabuleiro.getCasa(i, j);
            Peca pecaAlvo = alvo.getPeca();

            if (pecaAlvo != null) {
                pecasConsecutivas += 1;

                if (peca.isMesmaCor(pecaAlvo)) {
                    pecasAComer.clear();
                    return false;
                }

            } else {
                if (pecasConsecutivas == 1) {
                    Casa casa = tabuleiro.getCasa((alvo.getX() - sentidoX), (alvo.getY() - sentidoY));
                    pecasAComer.add(casa);
                }
                pecasConsecutivas = 0;
            }

            if (pecasConsecutivas == 2) {
                pecasAComer.clear();
                return false;
            }
        }
        return true;
    }

    public boolean percorrerEVerificar(Casa origem, int deltaX, int deltaY) {
        Peca peca = origem.getPeca();
        int x = origem.getX();
        int y = origem.getY();
        int pecasConsecutivas = 0;
        
        // SE FOR PEDRA
        if (!peca.isDama()) {
            x += deltaX;
            y += deltaY;
            
            try {
                Peca pecaAtual = tabuleiro.getCasa(x, y).getPeca();
                
                if (pecaAtual != null) {
                    if (tabuleiro.getCasa((x + deltaX), (y + deltaY)).getPeca() != null) {
                        return false;
                    }

                    if (peca.isMesmaCor(pecaAtual)) {
                        return false;
                    }

                    // if ((peca.getTipo() == Peca.PEDRA_BRANCA) && 
                    //     ((pecaAtual.getTipo() == Peca.DAMA_BRANCA || pecaAtual.getTipo() == Peca.PEDRA_BRANCA))) {
                    //         return false;
                    // } else {
                    //     if ((peca.getTipo() == Peca.PEDRA_VERMELHA) && 
                    //     ((pecaAtual.getTipo() == Peca.DAMA_VERMELHA || pecaAtual.getTipo() == Peca.PEDRA_VERMELHA))) {
                    //         return false;
                    //     }
                    
                    return true;
                }
            } catch (Exception e) {
                return false;
            }

        } else { // SE FOR DAMA
            while (!((x == -1 || x == 8) || (y == -1 || y == 8))) {
                x += deltaX;
                y += deltaY;
                
                try {
                    Peca pecaAtual = tabuleiro.getCasa(x, y).getPeca();
    
                    if (pecaAtual != null) {
                        pecasConsecutivas += 1;
                        if (peca.isMesmaCor(pecaAtual)) {
                            return false;
                        }
                    } else {
                        if (pecasConsecutivas == 1) return true;
                        if (pecasConsecutivas == 2) return false;
                    }
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean deveContinuarJogando(Casa origem) {
        if (percorrerEVerificar(origem, Tabuleiro.X_ESQUERDA, Tabuleiro.Y_CIMA)) return true;
        if (percorrerEVerificar(origem, Tabuleiro.X_DIREITA, Tabuleiro.Y_CIMA)) return true;
        if (percorrerEVerificar(origem, Tabuleiro.X_DIREITA, Tabuleiro.Y_BAIXO)) return true;
        if (percorrerEVerificar(origem, Tabuleiro.X_ESQUERDA, Tabuleiro.Y_BAIXO)) return true;
        return false;
    }

    public boolean podeTransformarParaDama(Casa casa) {
        Peca peca = casa.getPeca();
        if (peca == null) return false;
        
        return peca.podeVirarDama(casa.getY());
    }
}
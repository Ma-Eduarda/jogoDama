package com.damas.objetos;

/**
 * Representa uma Peça do jogo.
 * Possui uma casa e um tipo associado.
 * 
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 * @author José Alisson Rocha da Silva {@link jose.alisson2@academico.ufpb.br}
 */

public class Pedra implements Peca {

    protected Casa casa;
    protected int tipo;

    public Pedra(Casa casa, int tipo) {
        this.casa = casa;
        this.tipo = tipo;
        casa.colocarPeca(this);
    }
    
    @Override
    public void mover(Casa destino) {
        casa.removerPeca();
        destino.colocarPeca(this);
        casa = destino;
    }

    @Override
    public boolean isMovimentoValido(Casa destino) {
        int distanciaX = Math.abs(destino.getX() - casa.getX());
        int distanciaY = Math.abs(destino.getY() - casa.getY());

        if ((distanciaX == 0) || (distanciaY == 0)) return false;
        if ((distanciaX <= 2 || distanciaY <= 2) && (distanciaX == distanciaY)) {
            return true;
        }

        return false;
    }

    @Override
    public int getTipo() {
        return tipo;
    }

    @Override
    public boolean isMesmaCor(Peca outraPeca) {
        if (outraPeca == null) return false;

        boolean pecaBranca = (this.tipo == PEDRA_BRANCA || this.tipo == DAMA_BRANCA);
        boolean outraPecaBranca = (outraPeca.getTipo() == PEDRA_BRANCA || outraPeca.getTipo() == DAMA_BRANCA);

        if (pecaBranca && outraPecaBranca) {
            return true; 
        }
        if (!pecaBranca && !outraPecaBranca) {
            return true; 
        }
        
        return false; 
    }

    @Override
    public boolean isDirecaoValida(int sentidoY) {
        if (this.tipo == PEDRA_BRANCA && sentidoY == 1) return true; // Branca só sobe
        if (this.tipo == PEDRA_VERMELHA && sentidoY == -1) return true; // Vermelha só desce
        return false;
    }

    @Override
    public boolean isDama() {
        return false; 
    }

    @Override
    public boolean podeVirarDama(int y) {
        if (this.tipo == PEDRA_BRANCA && y == 7) return true;
        if (this.tipo == PEDRA_VERMELHA && y == 0) return true;
        return false;
    }
}
package com.damas.objetos;

/**
 * Dama do jogo.
 * <p>
 * Recebe uma casa e um tipo associado</p>
 *
 * @author João Victor da S. Cirilo {@link joao.cirilo@academico.ufpb.br}
 */
public class Dama extends Pedra {

    public Dama(Casa casa, int tipo) {
        super(casa, tipo);
    }

    @Override
    public boolean isMovimentoValido(Casa destino) {
        int distanciaX = Math.abs((destino.getX() - casa.getX()));
        int distanciaY = Math.abs((destino.getY() - casa.getY()));

        if (distanciaX == distanciaY) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isDama() {
        return true; 
    }

    @Override
    public boolean isDirecaoValida(int sentidoY) {
        return true; 
    }

    @Override
    public boolean podeVirarDama(int y) {
        return false;
    }
}

package com.unifor.order.DTO;

public class ProdutoResumidoDTO {

    private long id;
    private int quantidade;

    public ProdutoResumidoDTO() { };

    public ProdutoResumidoDTO(long id, int quantidade) {
        this.id = id;
        this.quantidade = quantidade;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}

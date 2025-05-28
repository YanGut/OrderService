package com.unifor.order.DTO;

public class ProdutoResponse {
    private Long id;
    private String name;
    private Long idFornecedor;

    public ProdutoResponse() { };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Long idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    // Getters e setters
}

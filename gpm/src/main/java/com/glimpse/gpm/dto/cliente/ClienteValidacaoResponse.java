package com.glimpse.gpm.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de validação de cliente duplicado ao solicitar orçamento")
public class ClienteValidacaoResponse {

    @Schema(description = "Indica se foi encontrado um cliente com email/telefone duplicado", example = "true")
    private boolean clienteEncontrado;

    @Schema(description = "Cliente existente encontrado (se houver)", example = "")
    private ClienteResponse cliente;

    @Schema(description = "Mensagem informando se cliente foi encontrado ou não", example = "Cliente já cadastrado com este email ou telefone")
    private String mensagem;

    public ClienteValidacaoResponse() {}

    public ClienteValidacaoResponse(boolean clienteEncontrado, ClienteResponse cliente, String mensagem) {
        this.clienteEncontrado = clienteEncontrado;
        this.cliente = cliente;
        this.mensagem = mensagem;
    }

    // Factory methods
    public static ClienteValidacaoResponse clienteEncontrado(ClienteResponse cliente) {
        return new ClienteValidacaoResponse(
            true,
            cliente,
            "Cliente já cadastrado com este email ou telefone. Deseja continuar com o mesmo cadastro ou criar um novo?"
        );
    }

    public static ClienteValidacaoResponse clienteNaoEncontrado() {
        return new ClienteValidacaoResponse(
            false,
            null,
            "Nenhum cliente encontrado. Será criado um novo cadastro."
        );
    }

    // Getters and Setters
    public boolean isClienteEncontrado() {
        return clienteEncontrado;
    }

    public void setClienteEncontrado(boolean clienteEncontrado) {
        this.clienteEncontrado = clienteEncontrado;
    }

    public ClienteResponse getCliente() {
        return cliente;
    }

    public void setCliente(ClienteResponse cliente) {
        this.cliente = cliente;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}

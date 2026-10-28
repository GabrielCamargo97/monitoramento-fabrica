package com.fabrica.monitoramento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class TelegramNotificador implements Notificador {

    private static final Logger log = LoggerFactory.getLogger(TelegramNotificador.class);

    private final RestClient client = RestClient.create();

    @Value("${telegram.token:}")
    private String token;

    @Value("${telegram.chat-id:}")
    private String chatId;

    @Override
    public void enviar(String mensagem) {
        if (token.isBlank() || chatId.isBlank()) {
            log.warn("Telegram não configurado. Alerta apenas no log: {}", mensagem);
            return;
        }
        try {
            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("chat_id", chatId);
            form.add("text", mensagem);

            client.post()
                    .uri("https://api.telegram.org/bot" + token + "/sendMessage")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .toBodilessEntity();
            
        } catch (org.springframework.web.client.RestClientResponseException e) {
            log.error("Telegram recusou o alerta: {} {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Falha ao enviar alerta ao Telegram ({})", e.getClass().getSimpleName());
        }
    }
}
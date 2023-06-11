package dk.dtu.compute.se.pisd.roborally.clients;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import dk.dtu.compute.se.pisd.roborally.model.MultiplayerModel;
import dk.dtu.compute.se.pisd.roborally.model.MultiplayerPlayerModel;
import reactor.core.publisher.Mono;

import java.util.List;

public class MultiplayerClient {
    private WebClient webClient;

    public MultiplayerClient(String ipAddress) {
        this.webClient = WebClient.create(ipAddress);
    }

    public void setTotalPlayers(int totalPlayers) {
        webClient.post()
                .uri("/multiplayer/setTotalPlayers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(totalPlayers), Integer.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public int getTotalPlayers() {
        return webClient.get()
                .uri("/multiplayer/totalPlayers")
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
    }

    public MultiplayerPlayerModel join(MultiplayerPlayerModel player) {
        return webClient.post()
                .uri("/multiplayer/join")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(player), MultiplayerPlayerModel.class)
                .retrieve()
                .bodyToMono(MultiplayerPlayerModel.class)
                .block();
    }

    public List<MultiplayerPlayerModel> getPlayers() {
        return webClient.get()
                .uri("/multiplayer/players")
                .retrieve()
                .bodyToFlux(MultiplayerPlayerModel.class)
                .collectList()
                .block();
    }

    public MultiplayerModel start() {
        return webClient.get()
                .uri("/multiplayer/start")
                .retrieve()
                .bodyToMono(MultiplayerModel.class)
                .block();
    }

    public int getPlayerTurn() {
        return webClient.get()
                .uri("/multiplayer/playerTurn")
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
    }

    public int nextPlayerTurn() {
        return webClient.get()
                .uri("/multiplayer/nextPlayerTurn")
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
    }
}

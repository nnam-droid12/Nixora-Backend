package com.nixora.loan.TrelloIntegration.service;

import com.nixora.auth.entities.User;
import com.nixora.loan.TrelloIntegration.config.TrelloClient;
import com.nixora.loan.TrelloIntegration.config.TrelloConfig;
import com.nixora.loan.TrelloIntegration.dto.PushToTrelloRequest;
import com.nixora.loan.TrelloIntegration.dto.TrelloBoardDTO;
import com.nixora.loan.TrelloIntegration.dto.TrelloListDTO;
import com.nixora.loan.TrelloIntegration.entity.TrelloOAuthToken;
import com.nixora.loan.TrelloIntegration.entity.TrelloUserConfig;
import com.nixora.loan.TrelloIntegration.repository.TrelloOAuthTokenRepository;
import com.nixora.loan.TrelloIntegration.repository.TrelloUserConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrelloService {

    private final TrelloOAuthTokenRepository tokenRepo;
    private final TrelloUserConfigRepository configRepo;
    private final TrelloClient trello;
    private final TrelloConfig config;
    private final RestTemplate rest = new RestTemplate();

    public void connect(User user, String token) {

        TrelloOAuthToken t = tokenRepo
                .findByUser(user)
                .orElse(new TrelloOAuthToken());

        t.setUser(user);
        t.setToken(token);

        tokenRepo.save(t);
    }

    public void pushField(User user, PushToTrelloRequest req) {

        TrelloOAuthToken token = tokenRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Trello not connected"));

        TrelloUserConfig cfg = configRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("No Trello list selected"));

        trello.createCard(
                token.getToken(),
                cfg.getListId(),
                req.getLabel(),
                req.getValue()
        );
    }


    public List<TrelloBoardDTO> getBoards(User user) {

        String token = tokenRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("User not connected to Trello"))
                .getToken();

        String url = "https://api.trello.com/1/members/me/boards"
                + "?key=" + config.getApiKey()
                + "&token=" + token;

        TrelloBoardDTO[] boards = rest.getForObject(url, TrelloBoardDTO[].class);
        return List.of(boards);
    }


    public List<TrelloListDTO> getLists(User user, String boardId) {

        String token = tokenRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("User not connected to Trello"))
                .getToken();

        String url = "https://api.trello.com/1/boards/" + boardId + "/lists"
                + "?key=" + config.getApiKey()
                + "&token=" + token;

        TrelloListDTO[] lists = rest.getForObject(url, TrelloListDTO[].class);
        return List.of(lists);
    }


    public void selectList(User user, String boardId, String listId) {

        TrelloUserConfig cfg = configRepo
                .findByUser(user)
                .orElse(new TrelloUserConfig());

        cfg.setUser(user);
        cfg.setBoardId(boardId);
        cfg.setListId(listId);

        configRepo.save(cfg);
    }

}

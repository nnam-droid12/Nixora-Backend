package com.nixora.loan.TrelloIntegration.controller;

import com.nixora.auth.entities.User;
import com.nixora.loan.TrelloIntegration.config.TrelloConfig;
import com.nixora.loan.TrelloIntegration.dto.PushToTrelloRequest;
import com.nixora.loan.TrelloIntegration.dto.TrelloBoardDTO;
import com.nixora.loan.TrelloIntegration.dto.TrelloListDTO;
import com.nixora.loan.TrelloIntegration.service.TrelloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static java.lang.Runtime.Version.parse;
import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
@RequestMapping("/api/trello")
@RequiredArgsConstructor
public class TrelloController {

    private final TrelloService service;
    private final TrelloConfig trelloConfig;

    @PostMapping("/push-field")
    public ResponseEntity<?> push(
            @AuthenticationPrincipal User user,
            @RequestBody PushToTrelloRequest req
    ) {
        service.pushField(user, req);
        return ResponseEntity.ok(Map.of("status", "sent"));
    }

    @PostMapping("/connect")
    public Map<String, String> connect() {

        String returnUrl = "https://nixora.onrender.com";;

        String url =
                "https://trello.com/1/authorize"
                        + "?expiration=never"
                        + "&name=Nixora"
                        + "&scope=read,write"
                        + "&response_type=token"
                        + "&key=" + trelloConfig.getApiKey()
                        + "&return_url=" + UriUtils.encode(returnUrl, StandardCharsets.UTF_8);

        return Map.of("url", url);
    }






    @GetMapping("/boards")
    public List<TrelloBoardDTO> getBoards(@AuthenticationPrincipal User user) {
        return service.getBoards(user);
    }


    @GetMapping("/boards/{boardId}/lists")
    public List<TrelloListDTO> getLists(
            @AuthenticationPrincipal User user,
            @PathVariable String boardId
    ) {
        return service.getLists(user, boardId);
    }

    @PostMapping("/select-list")
    public ResponseEntity<?> selectList(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, String> body
    ) {
        service.selectList(user, body.get("boardId"), body.get("listId"));
        return ResponseEntity.ok(Map.of("status", "List selected"));
    }


    @PostMapping("/callback")
    public ResponseEntity<?> callback(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, String> body
    ) {
        String token = body.get("token");

        service.connect(user, token);

        return ResponseEntity.ok(Map.of("status", "Trello connected"));
    }



}

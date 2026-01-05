package com.nixora.loan.TrelloIntegration.entity;

import com.nixora.auth.entities.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class TrelloUserConfig {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    private User user;

    private String boardId;
    private String listId;
}

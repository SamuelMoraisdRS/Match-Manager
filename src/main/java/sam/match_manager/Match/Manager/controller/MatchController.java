package sam.match_manager.Match.Manager.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import sam.match_manager.Match.Manager.messages.CreateMatchMessage;
import sam.match_manager.Match.Manager.messages.JoinMatchMessage;
import sam.match_manager.Match.Manager.messages.MatchData;
import sam.match_manager.Match.Manager.representations.MatchManager;
import sam.match_manager.Match.Manager.representations.Player;

import org.springframework.messaging.Message;

@Controller
public class MatchController {

  // TODO: Use a NoSQL DB
  private Map<String, MatchManager> matchManagers = new HashMap<>();

  private final SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  public MatchController(SimpMessagingTemplate simpMessagingTemplate) {
    this.simpMessagingTemplate = simpMessagingTemplate;
  }

  @MessageMapping("/create")
  @SendTo("/topics/created")
  public String createMatch(Message<CreateMatchMessage> message) {
    CreateMatchMessage createMatchMessage = message.getPayload();
    MatchData matchData = new MatchData(createMatchMessage.boardId(), createMatchMessage.numberOfChecks(),
        createMatchMessage.numberOfSubmits());
    MatchManager matchManager = new MatchManager(createMatchMessage.numberOfPlayers(), matchData);
    matchManagers.put(matchManager.getMatchCode(), matchManager);
    return matchManager.getMatchCode();
  }

  @MessageMapping("/join")
  public void joinMatch(Message<JoinMatchMessage> message) {
    String playerId = String.valueOf(message.getHeaders().get("simpSessionId")); // Probably won't work
    JoinMatchMessage joinMatchMessage = message.getPayload();
    String matchCode = String.valueOf(joinMatchMessage.matchCode());
    Player player = new Player(playerId, joinMatchMessage.playerStatus(), joinMatchMessage.numberOfGuesses(),
        joinMatchMessage.numberOfCorrects(), joinMatchMessage.checksUsed(), joinMatchMessage.submitsUsed());
    MatchManager matchManager = matchManagers.get(matchCode);
    matchManager.addPlayer(playerId, player);
    simpMessagingTemplate.convertAndSend("/topics/created/" + matchCode, matchManager.getMatchData());
  }
}

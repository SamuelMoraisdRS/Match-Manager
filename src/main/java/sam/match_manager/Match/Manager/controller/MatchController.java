package sam.match_manager.Match.Manager.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import sam.match_manager.Match.Manager.enums.MatchStatus;
import sam.match_manager.Match.Manager.messages.CreateMatchMessage;
import sam.match_manager.Match.Manager.messages.JoinMatchMessage;
import sam.match_manager.Match.Manager.messages.MatchData;
import sam.match_manager.Match.Manager.messages.HeartbeatMessage;
import sam.match_manager.Match.Manager.messages.HeartbeatMessage;
import sam.match_manager.Match.Manager.representations.MatchManager;
import sam.match_manager.Match.Manager.representations.Player;

import org.springframework.messaging.Message;

@Controller
public class MatchController {

  // TODO: Use a NoSQL DB or DataStore
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
    JoinMatchMessage joinMatchMessage = message.getPayload();
    String matchCode = String.valueOf(joinMatchMessage.matchCode());
    Player player = new Player(joinMatchMessage.playerStatus(), joinMatchMessage.numberOfGuesses(),
        joinMatchMessage.numberOfCorrects(), joinMatchMessage.checksUsed(), joinMatchMessage.submitsUsed());
    MatchManager matchManager = matchManagers.get(matchCode);
    matchManager.addPlayer(player);
    simpMessagingTemplate.convertAndSend("/topics/created/" + matchCode, player.getId());
  }

  @MessageMapping("/players/{matchCode}")
  public void getPlayers(@DestinationVariable("matchCode") String matchCode) {
    MatchManager matchManager = matchManagers.get(matchCode);
    simpMessagingTemplate.convertAndSend("/topics/created/players/" + matchCode, matchManager.getPlayers().values());
  }

  @MessageMapping("/heartbeat")
  public void receiveHeartbeat(Message<HeartbeatMessage> message) {
    HeartbeatMessage heartbeatMessage = message.getPayload();
    // TODO Replace with proper json creation on front end
    Player player = new Player(heartbeatMessage.playerId(), heartbeatMessage.playerStatus(),
        heartbeatMessage.numberOfGuesses(),
        heartbeatMessage.numberOfCorrects(),
        heartbeatMessage.checksUsed(), heartbeatMessage.submitsUsed());
    MatchManager matchManager = matchManagers.get(heartbeatMessage.matchCode());
    /*
     * ! This method is acting as an update and creation method, dependending
     * whether the player's id is provided or not. This should be refactored as soon
     * as possible.
     */
    matchManager.updatePlayer(player);
    if (matchManager.getMatchStatus() == MatchStatus.END) {
      simpMessagingTemplate.convertAndSend("/topics/created/heartbeat/" + heartbeatMessage.matchCode(), "over");
    } else {
      simpMessagingTemplate.convertAndSend("/topics/created/heartbeat/" + heartbeatMessage.matchCode(), "ongoing");
    }
  }
}

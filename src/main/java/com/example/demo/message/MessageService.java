package com.example.demo.message;

import com.example.demo.appUser.AppUser;
import com.example.demo.appUser.AppUserRepository;
import com.example.demo.game.Game;
import com.example.demo.game.GameRepository;
import com.example.demo.message.dto.AddMessageRequest;
import com.example.demo.message.dto.MessageResponse;
import com.example.demo.message.dto.UpdateMessageRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final GameRepository gameRepository;
    private final AppUserRepository appUserRepository;

    public Long addMessage(AddMessageRequest request) {
        if(this.gameRepository.findById(request.getGameId()).isPresent() && this.appUserRepository.findByEmail(request.getUserMail()).isPresent()){
            Game game = this.gameRepository.findById(request.getGameId()).get();
            AppUser appUser = this.appUserRepository.findByEmail(request.getUserMail()).get();

            Message newMessage = new Message(request.getText(), request.getDate(), game, appUser);

            this.messageRepository.save(newMessage);

            return newMessage.getId();
        }else{
            return -1L;
        }
    }

    public void updateMessage(UpdateMessageRequest request){
        if(this.messageRepository.findById(request.getMessageId()).isPresent()){
            Message message = this.messageRepository.findById(request.getMessageId()).get();

            message.setText(request.getText());
            message.setDateTime(request.getDate());

            this.messageRepository.save(message);
        }
    }

    public void deleteMessage(Long messageId){
        this.messageRepository.deleteById(messageId);
    }

    public MessageResponse getSingleMessage(Long messageId) {
        if(this.messageRepository.findById(messageId).isPresent()){
            Message message = this.messageRepository.findById(messageId).get();

            return new MessageResponse(message.getText(), message.getDateTime(), message.getAppUser().getEmail(), message.getGame().getId(), message.getId());
        }else{
            return new MessageResponse();
        }
    }

    public List<MessageResponse> getGameMessages(Long gameId) {
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();
            List<Message> gameMessages = this.messageRepository.findAllByGame(game);
            List<MessageResponse> response = new ArrayList<>();

            for(Message message : gameMessages){
                response.add(new MessageResponse(message.getText(), message.getDateTime(), message.getAppUser().getEmail(), gameId, message.getId()));
            }

            return response;
        }else{
            return new ArrayList<>();
        }
    }
}

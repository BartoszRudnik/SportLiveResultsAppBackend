package com.example.demo.message;

import com.example.demo.appUser.AppUser;
import com.example.demo.appUser.AppUserRepository;
import com.example.demo.game.Game;
import com.example.demo.game.GameRepository;
import com.example.demo.message.dto.AddMessageRequest;
import com.example.demo.message.dto.AddMessageResponse;
import com.example.demo.message.dto.MessageResponse;
import com.example.demo.message.dto.UpdateMessageRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final GameRepository gameRepository;
    private final AppUserRepository appUserRepository;

    public AddMessageResponse addReplyMessage(AddMessageRequest request, Long parentMessageId) {
        if(this.gameRepository.findById(request.getGameId()).isPresent() && this.appUserRepository.findByEmail(request.getUserMail()).isPresent() && this.messageRepository.findById(parentMessageId).isPresent()){
            Game game = this.gameRepository.findById(request.getGameId()).get();
            AppUser appUser = this.appUserRepository.findByEmail(request.getUserMail()).get();
            Message parentMessage = this.messageRepository.findById(parentMessageId).get();
            LocalDateTime date = LocalDateTime.now();

            Message newMessage = new Message(request.getText(), date, game, appUser, parentMessage);

            this.messageRepository.save(newMessage);

            return new AddMessageResponse(newMessage.getId(), date);
        }else{
            return new AddMessageResponse();
        }
    }

    public AddMessageResponse addMessage(AddMessageRequest request) {
        if(this.gameRepository.findById(request.getGameId()).isPresent() && this.appUserRepository.findByEmail(request.getUserMail()).isPresent()){
            Game game = this.gameRepository.findById(request.getGameId()).get();
            AppUser appUser = this.appUserRepository.findByEmail(request.getUserMail()).get();
            LocalDateTime date = LocalDateTime.now();

            Message newMessage = new Message(request.getText(), date, game, appUser);

            this.messageRepository.save(newMessage);

            return new AddMessageResponse(newMessage.getId(), date);
        }else{
            return new AddMessageResponse();
        }
    }

    public void updateMessage(UpdateMessageRequest request){
        if(this.messageRepository.findById(request.getMessageId()).isPresent()){
            Message message = this.messageRepository.findById(request.getMessageId()).get();
            message.setText(request.getText());

            this.messageRepository.save(message);
        }
    }

    public void deleteMessage(Long messageId){
        if(this.messageRepository.findById(messageId).isPresent()){
            Message message = this.messageRepository.findById(messageId).get();
            message.setDeleted(true);

            this.messageRepository.save(message);
        }
    }

    public MessageResponse getSingleMessage(Long messageId) {
        if(this.messageRepository.findById(messageId).isPresent()){
            Message message = this.messageRepository.findById(messageId).get();

            Long parentMessageId = -1L;

            if(message.getParentMessage() != null){
                parentMessageId = message.getParentMessage().getId();
            }

            return new MessageResponse(message.getText(), message.getDateTime(), message.getAppUser().getEmail(), message.getGame().getId(), message.getId(), message.isDeleted(), parentMessageId);
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
                Long parentMessageId = -1L;

                if(message.getParentMessage() != null){
                    parentMessageId = message.getParentMessage().getId();
                }

                response.add(new MessageResponse(message.getText(), message.getDateTime(), message.getAppUser().getEmail(), gameId, message.getId(), message.isDeleted(), parentMessageId));
            }

            return response;
        }else{
            return new ArrayList<>();
        }
    }
}

package com.example.demo.notification;
import com.example.demo.notification.dto.DeleteGameEventRequest;
import com.example.demo.notification.dto.NewEventDto;
import com.example.demo.notification.dto.NewTimeEventDto;
import com.example.demo.notification.dto.UpdateGameEventRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.List;

@Service
@Primary
@AllArgsConstructor
public class SseNotificationService implements NotificationService{
    private final EmitterRepository emitterRepository;
    private final EventMapper eventMapper;

    @Override
    public void sendNotification(Long gameId, Long messageId, String sub){
        this.doSendNotification(gameId, messageId, sub);
    }

    @Override
    public void sendNotification(String userMail, Long reportId) {
        this.doSendNotification(userMail, reportId);
    }

    @Override
    public void sendNotification(Long gameId, String subtitle) {this.doSendNotification(gameId, subtitle);}

    @Override
    public void sendNotification(Long gameId){
        this.doSendNotification(gameId);
    }

    @Override
    public void sendNotification(DeleteGameEventRequest request) {
        if(request != null){
            this.doSendNotification(request);
        }
    }

    @Override
    public void sendNotification(UpdateGameEventRequest request) {
        if(request != null){
            this.doSendNotification(request);
        }
    }

    @Override
    public void sendNotification(Long gameId, Long leagueId) {
        this.doSendNotification(gameId, leagueId);
    }

    @Override
    public void sendNotification(NewEventDto request) {
        if(request != null){
            this.doSendNotification(request);
        }
    }

    @Override
    public void sendNotification(NewTimeEventDto request){
        if(request != null){
            this.doSendNotification(request);
        }
    }

    private void doSendNotification(String userMail, Long reportId){
        if(this.emitterRepository.get(userMail + "report").isPresent()){
            List<SseEmitter> emitters = this.emitterRepository.get(userMail + "report").get();

            for(SseEmitter emitter : emitters){
                if(emitter != null){
                    try{
                        emitter.send(this.eventMapper.toSseEventBuilder(reportId));
                    }catch(IOException | IllegalStateException e){
//                        this.emitterRepository.remove(gameId + "reporter", emitter);
                    }
                }
            }
        }
    }

    private void doSendNotification(Long gameId, Long messageId, String sub){
        if(this.emitterRepository.get(gameId + "message").isPresent()){
            List<SseEmitter> emitters = this.emitterRepository.get(gameId + "message").get();

            for(SseEmitter emitter : emitters){
                if(emitter != null){
                    try{
                        emitter.send(this.eventMapper.toSseEventBuilder(gameId, messageId, sub));
                    }catch(IOException | IllegalStateException e){
//                        this.emitterRepository.remove(gameId + "reporter", emitter);
                    }
                }
            }
        }
    }

    private void doSendNotification(Long gameId, Long leagueId){
        if(this.emitterRepository.get(gameId + "reporter").isPresent()){
            List<SseEmitter> emitters = this.emitterRepository.get(gameId + "reporter").get();

            for(SseEmitter emitter : emitters){
                if(emitter != null){
                    try{
                        emitter.send(this.eventMapper.toSseEventBuilder(gameId, leagueId));
                    }catch(IOException | IllegalStateException e){
//                        this.emitterRepository.remove(gameId + "reporter", emitter);
                    }
                }
            }
        }
    }

    private void doSendNotification(UpdateGameEventRequest request){
        if(this.emitterRepository.get(Long.toString(request.getGameId())).isPresent()){
            List<SseEmitter> emitters = this.emitterRepository.get(Long.toString(request.getGameId())).get();

            for(SseEmitter emitter : emitters){
                if(emitter != null){
                    try{
                        emitter.send(this.eventMapper.toSseEventBuilder(request));
                    }catch (IOException | IllegalStateException e) {
//                        this.emitterRepository.remove(Long.toString(request.getGameId()), emitter);
                    }
                }
            }
        }
    }

    private void doSendNotification(DeleteGameEventRequest request){
        if(this.emitterRepository.get(Long.toString(request.getGameId())).isPresent()){
            List<SseEmitter> emitters = this.emitterRepository.get(Long.toString(request.getGameId())).get();

            for(SseEmitter emitter : emitters){
                if(emitter != null){
                    try{
                        emitter.send(this.eventMapper.toSseEventBuilder(request));
                    }catch (IOException | IllegalStateException e) {
//                        this.emitterRepository.remove(Long.toString(request.getGameId()), emitter);
                    }
                }
            }
        }
    }

    private void doSendNotification(Long gameId, String subtitle){
        if(this.emitterRepository.get(gameId + "lineup").isPresent()){
            List<SseEmitter> emitters = this.emitterRepository.get(gameId + "lineup").get();

            for(SseEmitter emitter : emitters){
                if(emitter != null){
                    try{
                        emitter.send(this.eventMapper.toSseEventBuilder(gameId, subtitle));
                    } catch (IOException | IllegalStateException e) {
//                        this.emitterRepository.remove(gameId + "stats", emitter);
                    }
                }
            }
        }
    }

    private void doSendNotification(Long gameId){
        if(this.emitterRepository.get(gameId + "stats").isPresent()){
            List<SseEmitter> emitters = this.emitterRepository.get(gameId + "stats").get();

            for(SseEmitter emitter : emitters){
                if(emitter != null){
                    try{
                        emitter.send(this.eventMapper.toSseEventBuilder(gameId));
                    } catch (IOException | IllegalStateException e) {
//                        this.emitterRepository.remove(gameId + "stats", emitter);
                    }
                }
            }
        }
    }

    private void doSendNotification(NewTimeEventDto request){
        if(this.emitterRepository.get(Long.toString(request.getGameId())).isPresent()){
            List<SseEmitter> emitters = this.emitterRepository.get(Long.toString(request.getGameId())).get();

            for(SseEmitter emitter : emitters){
                if(emitter != null) {
                    try {
                        emitter.send(this.eventMapper.toSseEventBuilder(request));
                    } catch (IOException | IllegalStateException e) {
//                        this.emitterRepository.remove(Long.toString(request.getGameId()), emitter);
                    }
                }
            }
        }
    }

    private void doSendNotification(NewEventDto request) {
        if(this.emitterRepository.get(Long.toString(request.getGameId())).isPresent()) {
            List<SseEmitter> emitters = this.emitterRepository.get(Long.toString(request.getGameId())).get();

            for (SseEmitter emitter : emitters) {
                if(emitter != null) {
                    try {
                        emitter.send(this.eventMapper.toSseEventBuilder(request));
                    } catch (IOException | IllegalStateException e) {
//                        this.emitterRepository.remove(Long.toString(request.getGameId()), emitter);
                    }
                }
            }
        }
    }
}

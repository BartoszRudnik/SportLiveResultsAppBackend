package com.example.demo.message;
import com.example.demo.message.dto.AddMessageRequest;
import com.example.demo.message.dto.MessageResponse;
import com.example.demo.message.dto.UpdateMessageRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/message")
@AllArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/addMessage")
    public Long addMessage(@RequestBody AddMessageRequest request){
        return this.messageService.addMessage(request);
    }

    @PostMapping("/updateMessage")
    public void updateMessage(@RequestBody UpdateMessageRequest request){
        this.messageService.updateMessage(request);
    }

    @PostMapping("/deleteMessage/{messageId}")
    public void deleteMessage(@PathVariable Long messageId){
        this.messageService.deleteMessage(messageId);
    }

    @GetMapping("/getSingleMessage/{messageId}")
    public MessageResponse getSingleMessage(@PathVariable Long messageId){
        return this.messageService.getSingleMessage(messageId);
    }

    @GetMapping("/getGameMessages/{gameId}")
    public List<MessageResponse> getGameMessages(@PathVariable Long gameId){
        return this.messageService.getGameMessages(gameId);
    }
}

package JSWD.Web.controler;

import JSWD.Web.model.Message;
import JSWD.Web.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    @GetMapping("/get/{x}")
    public ResponseEntity<Message> getMessageById(@PathVariable int x) {
        if (messageService.getMessageById(x).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(messageService.getMessageById(x).get());
    }
    @GetMapping("/get")
    public ResponseEntity<List<Message>> getEmployeeMessages() {
        if (messageService.getMessages().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(messageService.getMessages().get());
    }
    @PostMapping("/save")
    public void saveMessage(@RequestBody Message message) {
        messageService.saveMessage(message);
    }

}

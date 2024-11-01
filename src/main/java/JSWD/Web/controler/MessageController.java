package JSWD.Web.controler;

import JSWD.Web.model.chatSpecific.Message;
import JSWD.Web.model.comunication.JsonPayload;
import JSWD.Web.service.MessageService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RolesAllowed("ROLE_USER")
@RestController
@CrossOrigin(origins = "*")

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
    public ResponseEntity<List<Message>> getmessages() {

        if (messageService.getMessages().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(messageService.getMessages().get());
    }

    @PostMapping("/save")
    public void saveMessage(@RequestBody JsonPayload message) {
        messageService.saveMess(message);
    }

}

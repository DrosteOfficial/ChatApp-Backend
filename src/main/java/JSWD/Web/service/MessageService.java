package JSWD.Web.service;

import JSWD.Web.dao.IMessageRepository;
import JSWD.Web.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    IMessageRepository messageRepository;

    public MessageService(IMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    @Transactional
    public Optional<List<Message>> getMessages() {
        var Output = messageRepository.findAll().stream().limit(30).toList();
        if (Output.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Output);
    }
    @Transactional
    public Optional<Message> getMessageById(int messageId) {
        if (messageRepository.findById((long) messageId).isEmpty()) {
            return Optional.empty();
        }
        return messageRepository.findById((long) messageId);
    }
    @Transactional
    public Optional<Message> saveMessage(Message message) {
        messageRepository.save(message);
        return Optional.empty();
    }

}

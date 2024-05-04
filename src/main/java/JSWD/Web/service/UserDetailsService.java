package JSWD.Web.service;

import JSWD.Web.repositories.UserDetailsRepository;
import JSWD.Web.repositories.IImageRepository;
import JSWD.Web.repositories.IMessageRepository;
import JSWD.Web.model.UserDetails;
import JSWD.Web.model.Image;
import JSWD.Web.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserDetailsService {
    @Autowired
    UserDetailsRepository employeeRepository;
    @Autowired
    IMessageRepository messageRepository;
    @Autowired
    IImageRepository imageRepository;

    public UserDetailsService(UserDetailsRepository employeeRepository, IMessageRepository messageRepository, IImageRepository imageRepository) {
        this.employeeRepository = Objects.requireNonNull(employeeRepository);
        this.messageRepository = Objects.requireNonNull(messageRepository);
        this.imageRepository = Objects.requireNonNull(imageRepository);
    }

    @Transactional
    public Optional<List<UserDetails>> getAllEmployees() {
        if (employeeRepository.findAll().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(employeeRepository.findAll());
    }

    @Transactional
    public Optional<UserDetails> GetEmployeeById(int employeeId) {

        return employeeRepository.findById((long) employeeId);
    }

    @Transactional
    public Optional<UserDetails> SaveEmployee(UserDetails userDetails) {
        employeeRepository.save(userDetails);
        return Optional.empty();
    }


    @Transactional
    public Optional<UserDetails> DeleteEmployeeById(long id) {
        UserDetails userDetails = employeeRepository.findById(id).get();
        if (userDetails.isEmpty()) {
            return Optional.empty();
        }
        List<Message> messageList = userDetails.getMessages();
        messageRepository.deleteAll(messageList);
        var image = userDetails.getImagedata();
        imageRepository.delete(image);
        employeeRepository.deleteById((long) id);
        return Optional.of(userDetails);
    }

    @Transactional
    public Optional<List<Message>> getMessagesFromEmployee(int userId) {
        var employee = employeeRepository.findById((long) userId);
        if (employee.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(employee.get().getMessages().stream().toList());
    }

    @Transactional
    public Optional<Message> getMessageById(int messageId) {
        if (messageRepository.findById((long) messageId).isEmpty()) {
            return Optional.empty();
        }
        return messageRepository.findById((long) messageId);
    }

    @Transactional
    public Optional<Message> saveMessage(Message message, int userId) {
        if (message.getMessage().isEmpty()) {
            return Optional.empty();
        }
        messageRepository.save(message);
        UserDetails userDetails = employeeRepository.findById((long) userId).get();
        userDetails.addMessageToMessages(message);
        employeeRepository.save(userDetails);
        return Optional.of(message);
    }

    @Transactional
    public Optional<Message> deleteMessage(long messageId) {
        Message message = messageRepository.findById((long) messageId).get();
        if (message.getMessage().isEmpty()) {
            return Optional.empty();
        }
        UserDetails userDetails = (UserDetails) employeeRepository.findAll().stream().filter(userDetails1 -> userDetails1.getMessages().contains(message));
        messageRepository.deleteById(messageId);
        return Optional.of(message);
    }

    @Transactional
    public Optional<String> GetImageData(int userId) {
        return Optional.of(employeeRepository.findById((long) userId).get().getImagedata().getImageData());
    }

    @Transactional
    public Optional<String> saveUsersImage(String imageData, long userId) {
        if (imageData.isEmpty()) {
            return Optional.empty();
        }
        Image image = new Image(imageData);
        image = imageRepository.save(image);
        UserDetails userDetails = employeeRepository.findById(userId).get();
        userDetails.setImagedata(image);
        employeeRepository.save(userDetails);
        return Optional.of(imageData);
    }
//    @Transactional
//    public Optional<UserDetails> GetEmployeeByName(String name) {
//        if (employeeRepository.findAll().stream().filter(userDetails -> userDetails.getName().equals(name)).toList().isEmpty()) {
//            return Optional.empty();
//        }
//         return Optional.of(employeeRepository.findAll().stream().filter(userDetails -> userDetails.getName().equals(name)).toList().get(0));
//
//    }


}

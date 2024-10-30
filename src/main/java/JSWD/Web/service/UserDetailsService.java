//package JSWD.Web.service;
//
//import JSWD.Web.model.chatSpecific.Topic;
//import JSWD.Web.repositories.UserDetailsRepository;
//import JSWD.Web.repositories.chatSpecific.IImageRepository;
//import JSWD.Web.repositories.chatSpecific.IMessageRepository;
//import JSWD.Web.model.security.user.UserInformation;
//import JSWD.Web.model.chatSpecific.Image;
//import JSWD.Web.model.chatSpecific.Message;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
//@Service
//public class UserDetailsService {
//    @Autowired
//    UserDetailsRepository employeeRepository;
//    @Autowired
//    IMessageRepository messageRepository;
//    @Autowired
//    IImageRepository imageRepository;
//
//    public UserDetailsService(UserDetailsRepository employeeRepository, IMessageRepository messageRepository, IImageRepository imageRepository) {
//        this.employeeRepository = Objects.requireNonNull(employeeRepository);
//        this.messageRepository = Objects.requireNonNull(messageRepository);
//        this.imageRepository = Objects.requireNonNull(imageRepository);
//    }
//
//    @Transactional
//    public Optional<List<UserInformation>> getAllEmployees() {
//        if (employeeRepository.findAll().isEmpty()) {
//            return Optional.empty();
//        }
//        return Optional.of(employeeRepository.findAll());
//    }
//
//    @Transactional
//    public Optional<UserInformation> GetEmployeeById(int employeeId) {
//
//        return employeeRepository.findById((long) employeeId);
//    }
//
//    @Transactional
//    public Optional<UserInformation> SaveEmployee(UserInformation userInformation) {
//        employeeRepository.save(userInformation);
//        return Optional.empty();
//    }
//
//
////    @Transactional
////    public Optional<UserInformation> DeleteEmployeeById(long id) {
////        UserInformation userInformation = employeeRepository.findById(id).get();
////        if (userInformation.isEmpty()) {
////            return Optional.empty();
////        }
////        List<Message> messageList = userInformation.getMessages();
////        messageRepository.deleteAll(messageList);
////        var image = userInformation.getImagedata();
////        imageRepository.delete(image);
////        employeeRepository.deleteById((long) id);
////        return Optional.of(userInformation);
////    }
//
//    @Transactional
//    public Optional<List<Message>> getMessagesFromEmployee(int userId) {
//        var employee = employeeRepository.findById((long) userId);
//        if (employee.isEmpty()) {
//            return Optional.empty();
//        }
//        List<Message> messages = messageRepository.findAll().stream().filter(message -> message.getSenderId() == userId).toList();
//        return Optional.of(messages);
//    }
//
//    @Transactional
//    public Optional<Message> getMessageById(int messageId) {
//        if (messageRepository.findById((long) messageId).isEmpty()) {
//            return Optional.empty();
//        }
//        return messageRepository.findById((long) messageId);
//    }
//
//    @Transactional
//    public Optional<Message> saveMessage(Message message, int userId, String topicName) {
//        if (message.getMessage().isEmpty()) {
//            return Optional.empty();
//        }
//        messageRepository.save(message);
//        UserInformation userInformation = employeeRepository.findById((long) userId).get();
//        var topic = userInformation.getTopics().stream().filter(topics-> topics.getTopic().equals(topicName)).toList().get(0);
//        topic.getMessages().add(message);
//        employeeRepository.save(userInformation);
//        return Optional.of(message);
//    }
//
////    @Transactional
////    public Optional<Message> deleteMessage(long messageId) {
////        Message message = messageRepository.findById((long) messageId).get();
////        if (message.getMessage().isEmpty()) {
////            return Optional.empty();
////        }
////        UserInformation userInformation = employeeRepository.findAll().stream().filter(userDetails1 -> userDetails1.getMessages().contains(message));
////        messageRepository.deleteById(messageId);
////        return Optional.of(message);
////    }
//
//    @Transactional
//    public Optional<String> GetImageData(int userId) {
//        return Optional.of(employeeRepository.findById((long) userId).get().getImagedata().getImageData());
//    }
//
//    @Transactional
//    public Optional<String> saveUsersImage(String imageData, long userId) {
//        if (imageData.isEmpty()) {
//            return Optional.empty();
//        }
//        Image image = new Image(imageData);
//        image = imageRepository.save(image);
//        UserInformation userInformation = employeeRepository.findById(userId).get();
//        userInformation.setImagedata(image);
//        employeeRepository.save(userInformation);
//        return Optional.of(imageData);
//    }
////    @Transactional
////    public Optional<UserDetails> GetEmployeeByName(String name) {
////        if (employeeRepository.findAll().stream().filter(userDetails -> userDetails.getName().equals(name)).toList().isEmpty()) {
////            return Optional.empty();
////        }
////         return Optional.of(employeeRepository.findAll().stream().filter(userDetails -> userDetails.getName().equals(name)).toList().get(0));
////
////    }
//
//
//}


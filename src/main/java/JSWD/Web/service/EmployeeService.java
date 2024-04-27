package JSWD.Web.service;

import JSWD.Web.dao.IEmployeeRepository;
import JSWD.Web.dao.IImageRepository;
import JSWD.Web.dao.IMessageRepository;
import JSWD.Web.model.Employee;
import JSWD.Web.model.Image;
import JSWD.Web.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    IEmployeeRepository employeeRepository;
    @Autowired
    IMessageRepository messageRepository;
    @Autowired
    IImageRepository imageRepository;

    public EmployeeService(IEmployeeRepository employeeRepository, IMessageRepository messageRepository, IImageRepository imageRepository) {
        this.employeeRepository = Objects.requireNonNull(employeeRepository);
        this.messageRepository = Objects.requireNonNull(messageRepository);
        this.imageRepository = Objects.requireNonNull(imageRepository);
    }

    @Transactional
    public Optional<List<Employee>> getAllEmployees() {
        if (employeeRepository.findAll().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(employeeRepository.findAll());
    }

    @Transactional
    public Optional<Employee> GetEmployeeById(int employeeId) {

        return employeeRepository.findById((long) employeeId);
    }

    @Transactional
    public Optional<Employee> SaveEmployee(Employee employee) {
        employeeRepository.save(employee);
        return Optional.empty();
    }


    @Transactional
    public Optional<Employee> DeleteEmployeeById(long id) {
        Employee employee = employeeRepository.findById(id).get();
        if (employee.isEmpty()) {
            return Optional.empty();
        }
        List<Message> messageList = employee.getMessages();
        messageRepository.deleteAll(messageList);
        var image = employee.getImagedata();
        imageRepository.delete(image);
        employeeRepository.deleteById((long) id);
        return Optional.of(employee);
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
        Employee employee = employeeRepository.findById((long) userId).get();
        employee.addMessageToMessages(message);
        employeeRepository.save(employee);
        return Optional.of(message);
    }

    @Transactional
    public Optional<Message> deleteMessage(long messageId) {
        Message message = messageRepository.findById((long) messageId).get();
        if (message.getMessage().isEmpty()) {
            return Optional.empty();
        }
        Employee employee = (Employee) employeeRepository.findAll().stream().filter(employee1 -> employee1.getMessages().contains(message));
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
        Employee employee = employeeRepository.findById(userId).get();
        employee.setImagedata(image);
        employeeRepository.save(employee);
        return Optional.of(imageData);
    }


}

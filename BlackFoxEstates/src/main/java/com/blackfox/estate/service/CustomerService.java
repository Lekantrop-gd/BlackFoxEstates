package com.blackfox.estate.service;

import com.blackfox.estate.dto.CustomerDTO;
import com.blackfox.estate.entity.Customer;
import com.blackfox.estate.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Створення нового клієнта
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Optional<Customer> existingCustomer = customerRepository.findByEmail(customerDTO.email());
        if (existingCustomer.isPresent()) {
            throw new IllegalArgumentException("Customer with this email already exists");
        }

        Customer customer = new Customer(customerDTO.name(), customerDTO.email(), customerDTO.phoneNumber());
        customer = customerRepository.save(customer);

        return new CustomerDTO(customer.getId(), customer.getName(), customer.getEmail(), customer.getPhoneNumber());
    }

    // Отримання всіх клієнтів
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(c -> new CustomerDTO(c.getId(), c.getName(), c.getEmail(), c.getPhoneNumber()))
                .toList();
    }

    // Отримання клієнта за ID
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return new CustomerDTO(customer.getId(), customer.getName(), customer.getEmail(), customer.getPhoneNumber());
    }

    // Оновлення клієнта
    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setName(customerDTO.name());
        customer.setEmail(customerDTO.email());
        customer.setPhoneNumber(customerDTO.phoneNumber());

        customer = customerRepository.save(customer);

        return new CustomerDTO(customer.getId(), customer.getName(), customer.getEmail(), customer.getPhoneNumber());
    }

    // Видалення клієнта
    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found");
        }
        customerRepository.deleteById(id);
    }
}

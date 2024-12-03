package com.blackfox.estate.service;

import com.blackfox.estate.dto.CustomerDTO;
import com.blackfox.estate.entity.Customer;
import com.blackfox.estate.exception.ResourceNotFoundException;
import com.blackfox.estate.repository.CustomerRepository;
import com.blackfox.estate.mapper.CustomerMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public Page<CustomerDTO> getCustomers(String email, int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(parseSort(sort)));
        return customerRepository.findAll((root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (email != null) {
                predicates.add(criteriaBuilder.equal(root.get("email"), email));
            }
            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, pageable).map(customerMapper::toDTO);
    }

    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        logger.info("Creating customer with email: {}", customerDTO.email());
        if (customerRepository.existsByEmail(customerDTO.email())) {
            logger.warn("Customer creation failed. Email already exists: {}", customerDTO.email());
            throw new IllegalArgumentException("Email " + customerDTO.email() + " is already in use");
        }
        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerRepository.save(customer);
        logger.info("Customer created with ID: {}", customer.getId());
        return customerMapper.toDTO(customer);
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customerMapper.updateCustomerFromDTO(customerDTO, customer);
        customerRepository.save(customer);
        return customerMapper.toDTO(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    private List<Sort.Order> parseSort(String[] sort) {
        return Arrays.stream(sort)
                .map(s -> {
                    String[] parts = s.split(",");
                    return new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]);
                })
                .collect(Collectors.toList());
    }
}
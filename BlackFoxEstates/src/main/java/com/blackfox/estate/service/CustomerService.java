package com.blackfox.estate.service;

import com.blackfox.estate.dto.CustomerDTO;
import com.blackfox.estate.entity.Customer;
import com.blackfox.estate.mapper.CustomerMapper;
import com.blackfox.estate.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public Page<CustomerDTO> getCustomers(String email, int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));

        if (email != null) {
            return customerRepository.findAll((root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("email"), email), pageable)
                    .map(customerMapper::toDTO);
        } else {
            return customerRepository.findAll(pageable).map(customerMapper::toDTO);
        }
    }


    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDTO(savedCustomer);
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
        customerMapper.updateCustomerFromDTO(customerDTO, existingCustomer);
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.toDTO(updatedCustomer);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("Customer not found with ID: " + id);
        }
        customerRepository.deleteById(id);
    }

    private Sort parseSort(String[] sortParams) {
        if (sortParams == null || sortParams.length == 0) {
            return Sort.unsorted();
        }
        List<Sort.Order> orders = new ArrayList<>();
        for (String param : sortParams) {
            String[] split = param.split(",");
            if (split.length == 2) {
                String field = split[0];
                String direction = split[1];
                orders.add(new Sort.Order(Sort.Direction.fromString(direction), field));
            } else {
                throw new IllegalArgumentException("Invalid sort parameter: " + param);
            }
        }
        return Sort.by(orders);
    }
}
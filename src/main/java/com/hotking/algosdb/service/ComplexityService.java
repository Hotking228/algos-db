package com.hotking.algosdb.service;

import com.hotking.algosdb.repository.ComplexityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComplexityService {
    ComplexityRepository compRepository;
}

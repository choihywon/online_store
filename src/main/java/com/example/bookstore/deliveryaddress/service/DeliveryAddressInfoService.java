package com.example.bookstore.deliveryaddress.service;


import com.example.bookstore.deliveryaddress.domain.DeliveryAddressInfo;
import com.example.bookstore.deliveryaddress.dto.DeliveryAddressInfoDto;
import com.example.bookstore.deliveryaddress.repository.DeliveryAddressRepository;
import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryAddressInfoService {

    private final DeliveryAddressRepository deliveryAddressRepository;
    private final UserRepository userRepository;

    public List<DeliveryAddressInfoDto> findByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        return deliveryAddressRepository.findByUser(user)
                .stream()
                .map(DeliveryAddressInfoDto::new)
                .collect(Collectors.toList());
    }

    public void save(String email, DeliveryAddressInfoDto addressDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        DeliveryAddressInfo newAddress = DeliveryAddressInfo.createAddress(user, addressDto.getAddressName(),
                addressDto.getZipcode(), addressDto.getStreetAddr(), addressDto.getDetailAddr(), addressDto.getEtc());
        deliveryAddressRepository.save(newAddress);
    }

    public boolean checkDuplicateAddressName(String email, String addressName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        return deliveryAddressRepository.findByUser(user).stream()
                .anyMatch(address -> address.getAddressName().equals(addressName));
    }
}
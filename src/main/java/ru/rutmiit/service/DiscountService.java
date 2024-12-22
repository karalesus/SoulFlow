package ru.rutmiit.service;


import java.math.BigDecimal;
import java.util.UUID;

public interface DiscountService {

     BigDecimal calculateDiscount(UUID instructorId);

     BigDecimal calculateDiscountedPrice(BigDecimal originalPrice, BigDecimal discount);
}

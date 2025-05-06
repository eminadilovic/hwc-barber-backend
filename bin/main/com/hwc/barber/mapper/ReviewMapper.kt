package com.hwc.barber.mapper

import com.hwc.barber.dto.ReviewCreateDTO
import com.hwc.barber.dto.ReviewDTO
import com.hwc.barber.dto.ReviewUpdateDTO
import com.hwc.barber.model.Review
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface ReviewMapper {
    @Mapping(target = "shopId", source = "shop.id")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", expression = "java(review.getCustomer().getFirstName() + \" \" + review.getCustomer().getLastName())")
    @Mapping(target = "employeeId", source = "employee.id")
    fun toDTO(review: Review): ReviewDTO

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shop", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    fun toEntity(reviewCreateDTO: ReviewCreateDTO): Review

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shop", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    fun updateEntity(@MappingTarget review: Review, reviewUpdateDTO: ReviewUpdateDTO)
} 
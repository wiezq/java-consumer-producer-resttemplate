package com.example.consumer;

import com.example.consumer.dto.ProductDto;
import com.example.consumer.dto.ProductFilterRequest;
import com.example.consumer.dto.ProductResponseDto;
import com.example.consumer.dto.ReviewDto;
import com.example.consumer.service.ConsumerProductService;
import com.example.consumer.service.ConsumerReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;

@SpringBootApplication
@Slf4j
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}


	@Bean
	CommandLineRunner commandLineRunner(ConsumerProductService productService, ConsumerReviewService reviewService){
		return args -> {
			// Get all products
			ProductFilterRequest filterRequest = new ProductFilterRequest();
			filterRequest.setMinRating(3.0f);
			filterRequest.setMaxRating(5.0f);
			ProductResponseDto allProducts = productService.getAllProducts(
					0,
					5,
					"asc",
					"price", filterRequest).getBody();
			while (!allProducts.isLast()){
				log.info("{}", allProducts.getContent());
				allProducts = productService.getAllProducts(
						allProducts.getNumber() + 1,
						5,
						"asc",
						"price", filterRequest).getBody();
			}


			//Create a products
			ProductDto product1 = new ProductDto();
			product1.setName("New product1Response from consumer");
			product1.setDescription("Description product1Response from consumer");
			product1.setPrice(100L);
			product1.setCategoryId(1L);
			ResponseEntity<ProductDto> product1Response = productService.createProduct(product1);
			log.info("{}", product1Response.getBody());

			ProductDto product2 = new ProductDto();
			product2.setName("New product1Response from consumer 2");
			product2.setDescription("Description product2Response from consumer 2");
			product2.setPrice(1000000L);
			product2.setCategoryId(2L);
			ResponseEntity<ProductDto> product2Response = productService.createProduct(product2);
			log.info("{}", product2Response.getBody());


			// Create comments
			ReviewDto reviewDto = new ReviewDto();
			reviewDto.setComment("This is a comment consumer");
			reviewDto.setRating(5);
			ResponseEntity<ReviewDto> review = reviewService.createReview(product1Response.getBody().getId(), reviewDto);
			log.info("{}", review.getBody());

			ReviewDto reviewDto2 = new ReviewDto();
			reviewDto2.setComment("This is a comment consumer 2");
			reviewDto2.setRating(4);
			ResponseEntity<ReviewDto> review2 = reviewService.createReview(product2Response.getBody().getId(), reviewDto2);
			log.info("{}", review2.getBody());

		};
	}

}


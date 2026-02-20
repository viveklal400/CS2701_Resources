package com.example.demo;

import com.example.demo.Models.*;
import com.example.demo.Repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DatabaseInit implements CommandLineRunner{
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private ProduceRepository produceRepository;
	
	@Autowired
	private SellerProduceRepository sellerProduceRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Override
	 public void run(String... args) throws Exception {
		userRepository.deleteAll();
		produceRepository.deleteAll();
		sellerProduceRepository.deleteAll();
		orderRepository.deleteAll();
		orderItemRepository.deleteAll();
		
		// Step 1: Create and save users
		User bob = new User("Bob", "bob@sample.com", "bob_pass", UserType.BUYER);
		User prapanch = new User("Prapanch", "prapanch@sample.com", "prapanch_pass", UserType.SELLER);
		User ademola = new User("Ademola", "ademola@sample.com", "ademola_pass", UserType.BOTH);
		User zhixian = new User("Zhixian", "zhixian@sample.com", "zhixian_pass", UserType.BUYER);
		
		userRepository.save(bob);
		userRepository.save(prapanch);
		userRepository.save(ademola);
		userRepository.save(zhixian);
		
		// Step 2: Create and save produce
		Produce apple = new Produce("Apple");
		Produce lettuce = new Produce("Lettuce");
		Produce potatoes = new Produce("Potatoes");
		
		produceRepository.save(apple);
		produceRepository.save(lettuce);
		produceRepository.save(potatoes);
		
		// Step 3: Add selling/stock information (Seller Produce)
		// Prapanch sells apples for £0.15 (stock 100) and lettuce for £0.25 (stock 20)
		SellerProduce prapanch_apples = new SellerProduce(prapanch, apple, 0.15f, 100);
		SellerProduce prapanch_lettuce = new SellerProduce(prapanch, lettuce, 0.25f, 20);
		
		// Ademola sells apples for £0.30 (stock 50) and potatoes for £0.05 (stock 30)
		SellerProduce ademola_apples = new SellerProduce(ademola, apple, 0.30f, 50);
		SellerProduce ademola_potatoes = new SellerProduce(ademola, potatoes, 0.05f, 30);
		
		sellerProduceRepository.save(prapanch_apples);
		sellerProduceRepository.save(prapanch_lettuce);
		sellerProduceRepository.save(ademola_apples);
		sellerProduceRepository.save(ademola_potatoes);
		
		// Step 4: Create and save orders
		// Bob places an order for 2 apples from Ademola and 1 lettuce from Prapanch
		Order bob_order = new Order(bob);
		orderRepository.save(bob_order);
		
		OrderItem bob_apples = new OrderItem(bob_order, ademola_apples, 2, 0.30f);
		OrderItem bob_lettuce = new OrderItem(bob_order, prapanch_lettuce, 1, 0.25f);
		
		orderItemRepository.save(bob_apples);
		orderItemRepository.save(bob_lettuce);
		
		// Zhixian places an order for 10 apples from Prapanch and 15 potatoes from Ademola
		Order zhixian_order = new Order(zhixian);
		orderRepository.save(zhixian_order);
		
		OrderItem zhixian_apples = new OrderItem(zhixian_order, prapanch_apples, 10, 0.15f);
		OrderItem zhixian_potatoes = new OrderItem(zhixian_order, ademola_potatoes, 15, 0.05f);
		
		orderItemRepository.save(zhixian_apples);
		orderItemRepository.save(zhixian_potatoes);
	}
}

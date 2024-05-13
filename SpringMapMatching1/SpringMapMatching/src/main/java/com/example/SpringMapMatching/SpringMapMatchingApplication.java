package com.example.SpringMapMatching;

import com.example.SpringMapMatching.Database.Data;
import com.example.SpringMapMatching.Database.LocationNavPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SpringMapMatchingApplication implements CommandLineRunner {
	@Autowired
	private Data dataRepository ;

	public static void main(String[] args) {
		SpringApplication.run(SpringMapMatchingApplication.class, args);
	}

	@Override
	public void run(String... args) {
		List<LocationNavPath> allData = dataRepository.findAll();
//		System.out.println(allData.iterator());
		for (LocationNavPath data : allData) {
			System.out.println(data.getH_ID());
		}
	}
}

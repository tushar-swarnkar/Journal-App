package io.potato.journalApp;

import io.potato.journalApp.cache.AppCache;
import io.potato.journalApp.filter.JwtFilter;
import io.potato.journalApp.model.SentimentData;
import io.potato.journalApp.scheduler.UserScheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling // enables the scheduling of the methods
public class JournalApplication {

	public static void main(String[] args) {
		SpringApplication.run(JournalApplication.class, args);
	}

	@Bean
	public PlatformTransactionManager falana(MongoDatabaseFactory dbFactory) {
		return new MongoTransactionManager(dbFactory);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public AppCache appCache() {
		return new AppCache();
	}

	@Bean
	public JwtFilter jwtFilter() {
		return new JwtFilter();
	}

	@Bean
	public UserScheduler userScheduler() {
		return new UserScheduler();
	}

	@Bean
	public SentimentData sentimentData() {
		return new SentimentData();
	}
}

/*
	@EnableTransactionManagement:
		:: asks SPRING to search for all the methods with @Transactional annotation,
		:: then creates a TRANSACTIONAL CONTEXT for that method (like a container)
		:: all the operations inside that method will be treated as ONE

		>> interface PlatformTransactionManager:
			:: does the "rollback" & the "commit" operation
		>> MongoTransactionManager:
			:: implements the PlatformTransactionManager interface
*/


/*
	@Transactional annotation:
		:: it will treat all the operation as ONE OPERATION,
		:: meaning, if one of the operation 'fails', it will "rollback" all the successful commands before that, else "commit" all the changes to the DB

		>> achieves ATOMICITY: "fails" if one of the operation 'fails"; if all are 'successful' then it will also be "successful"
		>> achieves ISOLATION: if 2 diff users are using the same method, it will run 'separately' for both the users,
							 : for both users the methods is "isolated"
*/


package io.potato.journalApp.scheduler;

import io.potato.journalApp.cache.AppCache;
import io.potato.journalApp.entity.JournalEntry;
import io.potato.journalApp.entity.User;
import io.potato.journalApp.enums.Sentiment;
import io.potato.journalApp.model.SentimentData;
import io.potato.journalApp.repository.UserRepositoryImpl;
import io.potato.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private AppCache appCache;

    @Autowired
    private SentimentData sentimentData;

    /* Cron Expression syntax: second   minute   hour   day-of-month   month   day-of-week   year */
    @Scheduled(cron = "0 0 9 * * SUN") // 9 AM every Sunday
    public void fetchUserAndSendSaMail() {
        List<User> users = userRepository.getUserForSAs();
        for (User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());

            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();

            for (Sentiment sentiment : sentiments) {
                if (sentiment != null) {
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
                }
            }

            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            if (mostFrequentSentiment != null) {
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days " + mostFrequentSentiment).build();

                 try{
                     emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days", mostFrequentSentiment.toString());
                 } catch (Exception e) {
                     emailService.sendEmail(sentimentData.getEmail(), "Sentiment for previous week ", sentimentData.getSentiment());
                 }
            }

        }
    }

    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache() {
        appCache.init();
    }

}

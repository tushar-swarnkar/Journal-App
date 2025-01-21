package io.potato.journalApp.service;

import io.potato.journalApp.entity.JournalEntry;
import io.potato.journalApp.entity.User;
import io.potato.journalApp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public List<JournalEntry> findAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional // this saves the Error that might occur
    public void saveEntry(JournalEntry entry, String userName) {
        try {
            User user = userService.findByUserName(userName);
            entry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(entry);
//       ERROR might occur in this point: Entry is saved in the JournalEntries but is NOT associated with the user
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        } catch (Exception e) {
            log.error("Error! ", e);
            throw new RuntimeException("An error has occured while saving the entry: " + e);
        }

    }

    public void saveEntry(JournalEntry entry) {
        journalEntryRepository.save(entry);
    }

    @Transactional
    public boolean deleteById(ObjectId journalId, String userName) {
        boolean removed = false;
        try{
            User user = userService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(entry -> entry.getId().equals(journalId));
            if (removed) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(journalId);
            }
        } catch (Exception e) {
            log.error("Error!", e);
            throw new RuntimeException("An error occurred while deleting the entry.", e);
        }
        return removed;
    }

}

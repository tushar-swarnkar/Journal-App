package io.potato.journalApp.entity;

import io.potato.journalApp.enums.Sentiment;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@Document(collection = "journal_entries") // Equivalent to "Table" in MySQL
public class JournalEntry {

    @Id
    private ObjectId id;

    @NonNull
    private String title;

    private String content;
    private LocalDateTime date;

    private Sentiment sentiment;

}

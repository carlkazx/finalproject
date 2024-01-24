package com.example.newproject.services;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;


@Service
public class GoogleSheetsIntegrationService {

    private final TaskService taskService;
    private Sheets sheetsService;

    private static final Logger logger = LoggerFactory.getLogger(GoogleSheetsIntegrationService.class);

    @Autowired
    public GoogleSheetsIntegrationService(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostConstruct
    public void init() {
        try {
            this.sheetsService = createGoogleSheetsService();
        } catch (IOException | GeneralSecurityException e) {
            logger.error("Unable to initialize Google Sheets service", e);
            throw new IllegalStateException("Unable to initialize Google Sheets service", e);
        }
    }

    private Sheets createGoogleSheetsService() throws IOException, GeneralSecurityException {
        InputStream in = new ClassPathResource("phrasal-hold-390302-48006118f6c8.json").getInputStream();
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(in)
                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS_READONLY));
        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("Google-SheetsSample/0.1")
                .build();
    }

    @Scheduled(fixedRate = 30000) // 1 minute interval
    public void scheduledFetchFromGoogleSheets() {
        String spreadsheetId = "1Qk1ABikf0EedJGdjgcZtu-FtDfPAbu3QVNdjg1EtEnA"; // Replace with your actual spreadsheet ID
        String range = "'Form Responses 4'!A2:E"; // Replace with your actual range
        fetchAndProcessDataFromGoogleSheets(spreadsheetId, range);
    }

    public void fetchAndProcessDataFromGoogleSheets(String spreadsheetId, String range) {
        logger.info("Fetching data from Google Sheets: Spreadsheet ID - {}, Range - {}", spreadsheetId, range);
        try {
            ValueRange response = sheetsService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();

            List<List<Object>> values = response.getValues();
            if (values == null || values.isEmpty()) {
                logger.info("No data found in the Google Sheet.");
                return;
            }

            // Skip the header row by starting from the second row
            for (int i = 1; i < values.size(); i++) {
                List<Object> row = values.get(i);
                if (row.size() > 1 && row.get(1) != null && !row.get(1).toString().trim().isEmpty()) {
                    String ticketId = row.get(1).toString().trim();
                    // Check if the ticket number already exists in the database
                    if (!taskService.existsByTicketId(ticketId)) {
                        // Make sure you have enough columns for name, staffId, and details
                        if (row.size() >= 5) {
                            String timestamp = row.size() > 0 ? (String) row.get(0) : null; // Replace index if different
                            String name = row.size() > 2 ? (String) row.get(2) : null; // Replace index if different
                            String staffId = row.size() > 3 ? (String) row.get(3) : null; // Replace index if different
                            String details = row.size() > 4 ? (String) row.get(4) : null; // Replace index if different

                            // Log the information for debugging
                            logger.debug("Processing row: Ticket ID - {}, Name - {}, Staff ID - {}, Details - {}, Timestamp- {}", ticketId, name, staffId, details, timestamp);
                            // Call the service to create a task entity and save it to the database
                            taskService.createTaskFromGoogleSheet(ticketId, name, staffId, details, timestamp);
                        } else {
                            logger.warn("Insufficient data in row: {}", row);
                        }
                    } else {
                        logger.info("Ticket ID already exists in the database, skipping: {}", ticketId);
                    }
                } else {
                    logger.info("Row does not have a ticket number or ticket number is empty, skipping row: {}", i + 1);
                }
            }

        } catch (Exception e) {
            logger.error("Error fetching data from Google Sheets", e);
        }
    }

}
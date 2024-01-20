package com.example.newproject.services;

import com.example.newproject.entity.Task;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class GoogleSheetsService {

    private final Sheets sheetsService;
    private final TaskService taskService;

    @Autowired
    public GoogleSheetsService(Sheets sheetsService, TaskService taskService) {
        this.sheetsService = sheetsService;
        this.taskService = taskService;
    }

    public List<List<Object>> getSheetData(String spreadsheetId, String range) throws IOException {
        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        return response.getValues();
    }

    public void fetchNewTasks(String spreadsheetId, String range) {
        try {
            List<List<Object>> values = getSheetData(spreadsheetId, range);

            if (values == null || values.isEmpty()) {
                System.out.println("No new data found.");
            } else {
                for (List<Object> row : values) {
                    // Assuming the row structure matches the Task fields
                    Task newTask = new Task();
                    // Set the fields of newTask based on the contents of the row.
                    // Example:
                    // newTask.setTitle(row.get(0).toString());
                    // newTask.setDescription(row.get(1).toString());
                    // Continue setting the rest of the fields...

                    // Save the new task to your database
                    taskService.saveTask(newTask);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }

    // Additional methods...
}

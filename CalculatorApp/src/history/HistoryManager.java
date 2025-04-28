package history;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HistoryManager {
    private static final String HISTORY_FILE = "calculator_history.json";
    private static final int MAX_HISTORY_SIZE = 100;
    private static final Logger LOGGER = Logger.getLogger(HistoryManager.class.getName());
    private JSONArray history;
    private final Object lock = new Object();

    public HistoryManager() {
        history = loadHistory();
    }

    public void addHistory(String expression, String result) {
        synchronized (lock) {
            JSONObject entry = new JSONObject();
            entry.put("expression", expression);
            entry.put("result", result);
            entry.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            history.put(entry);

            while (history.length() > MAX_HISTORY_SIZE) {
                history.remove(0);
            }

            saveHistory();
        }
    }

    public List<String> getHistory() {
        synchronized (lock) {
            List<String> historyList = new ArrayList<>();
            for (int i = 0; i < history.length(); i++) {
                JSONObject entry = history.getJSONObject(i);
                String expr = entry.optString("expression", "N/A");
                String result = entry.optString("result", "N/A");
                String timestamp = entry.optString("timestamp", "Unknown");
                historyList.add(String.format("%s = %s (%s)", expr, result, timestamp));
            }
            return historyList;
        }
    }

    public void deleteHistoryAt(int index) {
        synchronized (lock) {
            if (index >= 0 && index < history.length()) {
                JSONArray newHistory = new JSONArray();
                for (int i = 0; i < history.length(); i++) {
                    if (i != index) {
                        newHistory.put(history.getJSONObject(i));
                    }
                }
                history = newHistory;
                saveHistory();
            }
        }
    }

    public void clearHistory() {
        synchronized (lock) {
            history = new JSONArray();
            saveHistory();
        }
    }

    public List<String> searchHistory(String keyword) {
        synchronized (lock) {
            List<String> results = new ArrayList<>();
            String lowerKeyword = keyword.toLowerCase();
            for (int i = 0; i < history.length(); i++) {
                JSONObject entry = history.getJSONObject(i);
                String expr = entry.optString("expression", "").toLowerCase();
                String result = entry.optString("result", "").toLowerCase();
                String timestamp = entry.optString("timestamp", "").toLowerCase();
                if (expr.contains(lowerKeyword) || result.contains(lowerKeyword) || timestamp.contains(lowerKeyword)) {
                    results.add(String.format("%s = %s (%s)",
                            entry.getString("expression"),
                            entry.getString("result"),
                            entry.getString("timestamp")));
                }
            }
            return results;
        }
    }

    private JSONArray loadHistory() {
        synchronized (lock) {
            try {
                File file = new File(HISTORY_FILE);
                if (!file.exists()) {
                    LOGGER.info("History file not found, creating new one: " + HISTORY_FILE);
                    file.createNewFile();
                    return new JSONArray();
                }

                String content = new String(Files.readAllBytes(file.toPath())).trim();
                if (content.isEmpty()) {
                    return new JSONArray();
                }

                return new JSONArray(content);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error loading history file: " + HISTORY_FILE, e);
                return new JSONArray();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error parsing JSON from history file: " + HISTORY_FILE, e);
                return new JSONArray();
            }
        }
    }

    private void saveHistory() {
        synchronized (lock) {
            try (FileWriter writer = new FileWriter(HISTORY_FILE)) {
                writer.write(history.toString(4));
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error saving history to file: " + HISTORY_FILE, e);
            }
        }
    }
}
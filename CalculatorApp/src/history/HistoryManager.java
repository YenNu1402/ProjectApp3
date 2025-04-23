package history;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {

    private static final String HISTORY_FILE = "calculator_history.json";
    private JSONArray history;

    public HistoryManager() {
        history = loadHistory();
    }

    public void addHistory(String expression, String result) {
        JSONObject entry = new JSONObject();
        entry.put("expression", expression);
        entry.put("result", result);
        history.put(entry);
        saveHistory();
    }

    public void deleteHistoryAt(int index) {
        if (index >= 0 && index < history.length()) {
            history.remove(index);
            saveHistory();
        }
    }

    public List<String> searchHistory(String keyword) {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < history.length(); i++) {
            JSONObject entry = history.getJSONObject(i);
            String expr = entry.optString("expression", "");
            String result = entry.optString("result", "");
            if (expr.contains(keyword) || result.contains(keyword)) {
                results.add(expr + " = " + result);
            }
        }
        return results;
    }

    public List<String> getHistory() {
        List<String> historyList = new ArrayList<>();
        for (int i = 0; i < history.length(); i++) {
            JSONObject entry = history.getJSONObject(i);
            historyList.add(entry.getString("expression") + " = " + entry.getString("result"));
        }
        return historyList;
    }

    private JSONArray loadHistory() {
        try {
            File file = new File(HISTORY_FILE);
            if (!file.exists()) {
                file.createNewFile();
                return new JSONArray();
            }

            String content = new String(Files.readAllBytes(file.toPath())).trim();
            if (content.isEmpty()) {
                return new JSONArray();
            }

            return new JSONArray(content);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private void saveHistory() {
        try (FileWriter writer = new FileWriter(HISTORY_FILE)) {
            writer.write(history.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

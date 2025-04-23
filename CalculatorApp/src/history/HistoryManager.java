package history;

import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    private List<String> history;

    public HistoryManager() {
        history = new ArrayList<>();
    }

    // Thêm lịch sử vào danh sách
    public void addHistory(String expression, String result) {
        history.add(expression + " = " + result);
    }

    // Lấy danh sách lịch sử
    public List<String> getHistory() {
        return history;
    }

    // Tìm kiếm lịch sử theo từ khoá
    public List<String> searchHistory(String keyword) {
        List<String> results = new ArrayList<>();
        for (String entry : history) {
            if (entry.contains(keyword)) {
                results.add(entry);
            }
        }
        return results;
    }

    // Xóa lịch sử tại chỉ mục nhất định
    public void deleteHistoryAt(int index) {
        if (index >= 0 && index < history.size()) {
            history.remove(index);
        }
    }
}

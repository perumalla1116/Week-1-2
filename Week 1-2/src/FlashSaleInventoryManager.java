import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class FlashSaleInventoryManager {

    private Map<String, Integer> stock = new HashMap<>();
    private Map<String, LinkedHashMap<Integer, Boolean>> waitingList = new HashMap<>();

    public FlashSaleInventoryManager() {
        stock.put("IPHONE15_256GB", 100);
        waitingList.put("IPHONE15_256GB", new LinkedHashMap<>());
    }

    public int checkStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }

    public synchronized String purchaseItem(String productId, int userId) {

        int currentStock = stock.getOrDefault(productId, 0);

        if (currentStock > 0) {
            stock.put(productId, currentStock - 1);
            return "Success, " + (currentStock - 1) + " units remaining";
        }

        // Create waiting list if not present
        waitingList.putIfAbsent(productId, new LinkedHashMap<>());

        LinkedHashMap<Integer, Boolean> queue = waitingList.get(productId);

        // Prevent duplicate users in waiting list
        if (!queue.containsKey(userId)) {
            queue.put(userId, true);
        }

        int position = queue.size();
        return "Added to waiting list, position #" + position;
    }

    public static void main(String[] args) {

        FlashSaleInventoryManager manager = new FlashSaleInventoryManager();

        System.out.println(manager.checkStock("IPHONE15_256GB"));

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        for (int i = 0; i < 100; i++) {
            manager.purchaseItem("IPHONE15_256GB", i);
        }

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));
    }
}
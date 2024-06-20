
import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.MongoCursor;
import java.util.Scanner;

class Item{
    private String Id;
    private String Name;
    private int Quantity;

    public Item(String Id,String Name, int Quantity) {
        this.Id = Id;
        this.Name= Name;
        this.Quantity= Quantity;
    }
    public String getId(){
        return Id;
    }
    public String getName(){
        return Name;
    }
    public int getQuantity(){
        return Quantity;

}

}
class MongoDBConnection {
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    public static void connect() {
        MongoClient mongoClient =  MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("sales");
        collection = database.getCollection("inventory");
    }

    public static MongoCollection<Document> getCollection() {
        return collection;
    }
}
class InventoryManager {
    private MongoCollection<Document> collection;

    public InventoryManager() {
        MongoDBConnection.connect();
        this.collection = MongoDBConnection.getCollection();
    }

    public void addItem(Item item) {
      Document document = new Document("Id",item.getId())
        .append("Name", item.getName())
        .append("Quantity", item.getQuantity());
        collection.insertOne(document);
    }
    public void updateItemQuantity(String Id, int Quantity) {
        Document query = new Document("Id", Id);
        Document newdoc = new Document("$set",new Document("Quantity", Quantity));
        collection.updateOne(query, newdoc);
    }
      public void deleteItem(String Id) {
        Document query = new Document("Id",Id);
        collection.deleteOne(query);
      }
      public void viewInventory() {
          MongoCursor<Document> cursor = collection.find().iterator();
          try {
              while (cursor.hasNext()) {
                  Document document = cursor.next();
                  System.out.println("ID: " + document.getString("Id") +
                          ", Name: " + document.getString("Name") +
                          ", Quantity: " + document.getInteger("Quantity"));
              }
              } finally{
                  cursor.close();
              }
          }
      }

public class InventoryApp {
    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Add Item");
            System.out.println("2. Update Item Quantity");
            System.out.println("3. Remove Item");
            System.out.println("4. View Inventory");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter item id: ");
                    String Id = scanner.nextLine();
                    System.out.print("Enter item name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter item quantity: ");
                    int Quantity = scanner.nextInt();
                    manager.addItem(new Item(Id, name, Quantity));
                    System.out.println("Added!!");
                    break;

                case 2:
                    System.out.print("Enter item id: ");
                    Id = scanner.nextLine();
                    System.out.print("Enter new quantity: ");
                    Quantity = scanner.nextInt();
                    manager.updateItemQuantity(Id, Quantity);
                    System.out.println("Updated!!");
                    break;

                case 3:
                    System.out.print("Enter item id: ");
                    Id = scanner.nextLine();
                    manager.deleteItem(Id);
                    System.out.println("Removed!!");
                    break;

                case 4:
                    manager.viewInventory();
                    break;

                case 5:
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}


package kurs_auto;

import java.io.*;
import java.util.*;

// Клас відділення реалізує інтерфейс Serializable для збереження у файл
class ServiceDepartment implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nameDep; 
    private LinkedHashMap<String, Integer> priceList; 

    public ServiceDepartment(String name) {
        this.nameDep = name;
        this.priceList = new LinkedHashMap<>();
    }

    public String getName() {
        return nameDep;
    }

    public void setName(String name) {
        this.nameDep = name;
    }

    public void addService(String serviceName, int cost) {
        priceList.put(serviceName, cost);
    }

    public void removeService(String serviceName) {
        priceList.remove(serviceName);
    }

    public LinkedHashMap<String, Integer> getPriceList() {
        return priceList;
    }
}

// Клас автосервісу містить список відділень (Композиція)
class AutoService implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private ArrayList<ServiceDepartment> departments;

    public AutoService(String name) {
        this.name = name;
        this.departments = new ArrayList<>();
        initializeDefaultData();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ServiceDepartment> getDepartments() {
        return departments;
    }

    public void addDepartment(ServiceDepartment dep) {
        departments.add(dep);
    }

    public void removeDepartment(int index) {
        if (index >= 0 && index < departments.size()) {
            departments.remove(index);
        }
    }

    // Заповнення початковими даними, якщо файл не знайдено
    private void initializeDefaultData() {
        ServiceDepartment dep1 = new ServiceDepartment("Шиномонтаж");
        dep1.addService("Балансування коліс", 200);
        dep1.addService("Заміна шини", 150);
        
        ServiceDepartment dep2 = new ServiceDepartment("Діагностика");
        dep2.addService("Комп'ютерна діагностика", 500);
        dep2.addService("Діагностика ходової", 400);

        departments.add(dep1);
        departments.add(dep2);
    }
}

// Допоміжний клас для зберігання позиції в кошику клієнта
class CartItem {
    String serviceName;
    int unitPrice;
    int quantity;

    public CartItem(String serviceName, int unitPrice) {
        this.serviceName = serviceName;
        this.unitPrice = unitPrice;
        this.quantity = 1;
    }
}

class InterfaceManager {
    private AutoService auto;
    private Scanner scanner;
    private final String DATA_FILE = "autoservice_data.ser";

    public InterfaceManager() {
        scanner = new Scanner(System.in, "UTF-8");
        loadData();
    }

    // Метод очищення консолі
    private void clearConsole() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 30; i++) System.out.println(); // Альтернативне очищення
        }
    }

    // Метод очікування дії користувача
    private void pause() {
        System.out.println("\n[Натисніть Enter для продовження...]");
        scanner.nextLine();
    }

    // Метод для безпечного зчитування цілих чисел (обробка виключень)
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); 
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Помилка: введіть коректне ціле число.");
                scanner.nextLine(); 
            }
        }
    }

    public void start() {
        boolean running = true;
        while (running) {
            clearConsole();
            System.out.println("=== ГОЛОВНЕ МЕНЮ (" + auto.getName() + ") ===");
            System.out.println("1. Режим 'Клієнт'");
            System.out.println("2. Режим 'Менеджер'");
            System.out.println("0. Вихід з програми");
            
            int choice = readInt("Оберіть дію: ");
            switch (choice) {
                case 1:
                    clientMenu();
                    break;
                case 2:
                    managerMenu();
                    break;
                case 0:
                    saveData();
                    System.out.println("Роботу завершено.");
                    running = false;
                    break;
                default:
                    System.out.println("Некоректний вибір.");
                    pause();
            }
        }
    }

    // ================= РЕЖИМ МЕНЕДЖЕРА =================
    private void managerMenu() {
        boolean inManager = true;
        while (inManager) {
            clearConsole();
            System.out.println("--- ПАНЕЛЬ МЕНЕДЖЕРА ---");
            System.out.println("1. Переглянути структуру автосервісу");
            System.out.println("2. Додати нове відділення");
            System.out.println("3. Видалити відділення");
            System.out.println("4. Керувати послугами у відділенні");
            System.out.println("5. Змінити назву автосервісу");
            System.out.println("0. Повернутися до головного меню");

            int choice = readInt("Ваш вибір: ");
            switch (choice) {
                case 1:
                    clearConsole();
                    displayStructure();
                    pause();
                    break;
                case 2:
                    addDepartmentFlow();
                    break;
                case 3:
                    deleteDepartmentFlow();
                    break;
                case 4:
                    manageServicesFlow();
                    break;
                case 5:
                    System.out.print("Введіть нову назву автосервісу: ");
                    auto.setName(scanner.nextLine());
                    System.out.println("Назву успішно змінено.");
                    pause();
                    break;
                case 0:
                    inManager = false;
                    break;
                default:
                    System.out.println("Некоректний вибір.");
                    pause();
            }
        }
    }

    private void displayStructure() {
        System.out.println("--- СТРУКТУРА: " + auto.getName() + " ---");
        ArrayList<ServiceDepartment> deps = auto.getDepartments();
        if (deps.isEmpty()) {
            System.out.println("Відділення відсутні.");
            return;
        }
        for (int i = 0; i < deps.size(); i++) {
            System.out.println("[" + (i + 1) + "] Відділення: " + deps.get(i).getName());
            for (Map.Entry<String, Integer> entry : deps.get(i).getPriceList().entrySet()) {
                System.out.println("    - " + entry.getKey() + " (" + entry.getValue() + " грн)");
            }
        }
    }

    private void addDepartmentFlow() {
        clearConsole();
        System.out.print("Введіть назву нового відділення: ");
        String name = scanner.nextLine();
        auto.addDepartment(new ServiceDepartment(name));
        System.out.println("Відділення додано.");
        pause();
    }

    private void deleteDepartmentFlow() {
        clearConsole();
        displayStructure();
        int index = readInt("\nВведіть номер відділення для видалення (0 для відміни): ") - 1;
        if (index >= 0 && index < auto.getDepartments().size()) {
            auto.removeDepartment(index);
            System.out.println("Відділення видалено.");
        }
        pause();
    }

    private void manageServicesFlow() {
        clearConsole();
        displayStructure();
        int depIndex = readInt("\nОберіть відділення для керування послугами (0 для відміни): ") - 1;
        if (depIndex < 0 || depIndex >= auto.getDepartments().size()) return;

        ServiceDepartment dep = auto.getDepartments().get(depIndex);
        boolean managing = true;
        while (managing) {
            clearConsole();
            System.out.println("--- Послуги відділення: " + dep.getName() + " ---");
            List<String> sKeys = new ArrayList<>(dep.getPriceList().keySet());
            
            if (sKeys.isEmpty()) {
                System.out.println("Послуги відсутні.");
            } else {
                for (int i = 0; i < sKeys.size(); i++) {
                    System.out.println("[" + (i + 1) + "] " + sKeys.get(i) + " - " + dep.getPriceList().get(sKeys.get(i)) + " грн");
                }
            }

            System.out.println("\n1. Додати послугу");
            System.out.println("2. Видалити послугу");
            System.out.println("3. Редагувати послугу");
            System.out.println("0. Назад");

            int choice = readInt("Ваш вибір: ");
            switch (choice) {
                case 1:
                    boolean adding = true;
                    while (adding) {
                        System.out.print("Назва послуги: ");
                        String sName = scanner.nextLine();
                        
                        if (dep.getPriceList().containsKey(sName)) {
                            System.out.println("\nУВАГА! Послуга з такою назвою вже існує у цьому відділенні.");
                            System.out.println("1. Замінити існуючу послугу (оновити ціну)");
                            System.out.println("2. Ввести іншу назву");
                            System.out.println("0. Скасувати додавання");
                            int conflictChoice = readInt("Ваш вибір: ");
                            
                            if (conflictChoice == 1) {
                                int sPrice = readInt("Нова ціна (грн): ");
                                dep.addService(sName, sPrice);
                                System.out.println("Послугу оновлено.");
                                adding = false;
                            } else if (conflictChoice == 2) {
                                continue; // Повертаємось на початок циклу введення назви
                            } else {
                                System.out.println("Операцію скасовано.");
                                adding = false;
                            }
                        } else {
                            int sPrice = readInt("Ціна (грн): ");
                            dep.addService(sName, sPrice);
                            System.out.println("Послугу успішно додано.");
                            adding = false;
                        }
                    }
                    pause();
                    break;
                case 2:
                    if (sKeys.isEmpty()) {
                        System.out.println("Немає послуг для видалення.");
                        pause();
                        break;
                    }
                    int delIndex = readInt("Введіть номер послуги для видалення: ") - 1;
                    if (delIndex >= 0 && delIndex < sKeys.size()) {
                        dep.removeService(sKeys.get(delIndex));
                        System.out.println("Послугу видалено.");
                    } else {
                        System.out.println("Некоректний номер.");
                    }
                    pause();
                    break;
                case 3:
                    if (sKeys.isEmpty()) {
                        System.out.println("Немає послуг для редагування.");
                        pause();
                        break;
                    }
                    int editIndex = readInt("Введіть номер послуги для редагування: ") - 1;
                    if (editIndex >= 0 && editIndex < sKeys.size()) {
                        String oldName = sKeys.get(editIndex);
                        System.out.print("Нова назва (натисніть Enter, щоб залишити '" + oldName + "'): ");
                        String newName = scanner.nextLine();
                        if (newName.trim().isEmpty()) {
                            newName = oldName;
                        }
                        
                        // Захист від перезапису іншої існуючої послуги при редагуванні
                        if (!newName.equals(oldName) && dep.getPriceList().containsKey(newName)) {
                            System.out.println("\nПомилка: послуга з назвою '" + newName + "' вже існує. Використовуйте інші назви для різних послуг.");
                            System.out.println("Редагування скасовано.");
                        } else {
                            int newPrice = readInt("Нова ціна (грн): ");
                            dep.removeService(oldName);
                            dep.addService(newName, newPrice);
                            System.out.println("Послугу успішно оновлено.");
                        }
                    } else {
                        System.out.println("Некоректний номер.");
                    }
                    pause();
                    break;
                case 0:
                    managing = false;
                    break;
                default:
                    System.out.println("Некоректний вибір.");
                    pause();
            }
        }
    }

    // ================= РЕЖИМ КЛІЄНТА =================
    private void clientMenu() {
        clearConsole();
        System.out.print("Введіть ваше ім'я: ");
        String clientName = scanner.nextLine();

        ArrayList<ServiceDepartment> deps = auto.getDepartments();
        if (deps.isEmpty()) {
            System.out.println("Вибачте, наразі немає доступних відділень.");
            pause();
            return;
        }

        // Кошик клієнта: Ключ - назва відділення, Значення - Мапа (назва послуги -> об'єкт CartItem)
        LinkedHashMap<String, LinkedHashMap<String, CartItem>> cart = new LinkedHashMap<>();
        boolean ordering = true;

        while (ordering) {
            clearConsole();
            
            // Підрахунок загальних значень кошика для відображення
            int totalSum = 0;
            int itemsCount = 0;
            for (LinkedHashMap<String, CartItem> depCart : cart.values()) {
                for (CartItem item : depCart.values()) {
                    totalSum += (item.unitPrice * item.quantity);
                    itemsCount += item.quantity;
                }
            }

            // Вивід кошика
            System.out.println("=== ВАШ КОШИК (" + itemsCount + " позицій на суму " + totalSum + " грн) ===");
            if (cart.isEmpty()) {
                System.out.println("  [Кошик порожній]");
            } else {
                for (Map.Entry<String, LinkedHashMap<String, CartItem>> entry : cart.entrySet()) {
                    System.out.println("Відділення [" + entry.getKey() + "]:");
                    for (CartItem item : entry.getValue().values()) {
                        int rowTotal = item.unitPrice * item.quantity;
                        if (item.quantity > 1) {
                            System.out.println("  - " + item.serviceName + " x" + item.quantity + " ........... " + rowTotal + " грн (по " + item.unitPrice + " грн)");
                        } else {
                            System.out.println("  - " + item.serviceName + " ........... " + rowTotal + " грн");
                        }
                    }
                }
            }
            System.out.println("=========================================================");

            System.out.println("\nДії з кошиком:");
            System.out.println("1. Додати послугу (Перейти до списку відділень)");
            System.out.println("2. Видалити послугу з кошика");
            System.out.println("0. Завершити замовлення та сформувати чек");

            int action = readInt("Ваш вибір: ");
            
            switch (action) {
                case 1:
                    addServiceToCartFlow(deps, cart);
                    break;
                case 2:
                    removeServiceFromCartFlow(cart);
                    break;
                case 0:
                    ordering = false;
                    break;
                default:
                    System.out.println("Некоректний вибір.");
                    pause();
            }
        }

        if (!cart.isEmpty()) {
            clearConsole();
            int finalTotal = 0;
            for (LinkedHashMap<String, CartItem> depCart : cart.values()) {
                for (CartItem item : depCart.values()) {
                    finalTotal += (item.unitPrice * item.quantity);
                }
            }
            generateReceipt(clientName, cart, finalTotal);
        } else {
            System.out.println("\nВи нічого не замовили. Скасування.");
        }
        pause();
    }

    // Підменю додавання послуги до кошика
    private void addServiceToCartFlow(ArrayList<ServiceDepartment> deps, LinkedHashMap<String, LinkedHashMap<String, CartItem>> cart) {
        clearConsole();
        System.out.println("Оберіть відділення:");
        for (int i = 0; i < deps.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + deps.get(i).getName());
        }
        System.out.println("[0] Назад");

        int depChoice = readInt("Ваш вибір: ") - 1;
        if (depChoice == -1) return;
        if (depChoice < 0 || depChoice >= deps.size()) {
            System.out.println("Некоректний вибір.");
            pause();
            return;
        }

        ServiceDepartment chosenDep = deps.get(depChoice);
        LinkedHashMap<String, Integer> services = chosenDep.getPriceList();
        
        if (services.isEmpty()) {
            System.out.println("У цьому відділенні наразі немає послуг.");
            pause();
            return;
        }

        clearConsole();
        System.out.println("--- Послуги відділення: " + chosenDep.getName() + " ---");
        List<String> serviceKeys = new ArrayList<>(services.keySet());
        for (int i = 0; i < serviceKeys.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + serviceKeys.get(i) + " - " + services.get(serviceKeys.get(i)) + " грн");
        }
        System.out.println("\n[0] Назад");

        int sChoice = readInt("Ваш вибір: ") - 1;
        if (sChoice == -1) return;
        
        if (sChoice >= 0 && sChoice < serviceKeys.size()) {
            String selectedService = serviceKeys.get(sChoice);
            int price = services.get(selectedService);
            
            LinkedHashMap<String, CartItem> depCart = cart.getOrDefault(chosenDep.getName(), new LinkedHashMap<>());
            
            if (depCart.containsKey(selectedService)) {
                depCart.get(selectedService).quantity++;
            } else {
                depCart.put(selectedService, new CartItem(selectedService, price));
            }
            
            cart.put(chosenDep.getName(), depCart);
            System.out.println("\nПослугу '" + selectedService + "' успішно додано до замовлення!");
            pause();
        } else {
            System.out.println("Некоректний вибір послуги.");
            pause();
        }
    }

    // Підменю видалення послуги з кошика
    private void removeServiceFromCartFlow(LinkedHashMap<String, LinkedHashMap<String, CartItem>> cart) {
        if (cart.isEmpty()) {
            System.out.println("Кошик порожній, видаляти нічого.");
            pause();
            return;
        }

        clearConsole();
        System.out.println("=== ВИДАЛЕННЯ З КОШИКА ===");
        
        List<String> flatDepNames = new ArrayList<>();
        List<CartItem> flatItems = new ArrayList<>();
        int idx = 1;
        
        for (Map.Entry<String, LinkedHashMap<String, CartItem>> depEntry : cart.entrySet()) {
            System.out.println("Відділення [" + depEntry.getKey() + "]:");
            for (CartItem item : depEntry.getValue().values()) {
                flatDepNames.add(depEntry.getKey());
                flatItems.add(item);
                System.out.println("  [" + idx + "] " + item.serviceName + " x" + item.quantity);
                idx++;
            }
        }
        
        System.out.println("\n[0] Відміна");
        int delChoice = readInt("Оберіть номер позиції для видалення: ") - 1;
        
        if (delChoice == -1) return;
        
        if (delChoice >= 0 && delChoice < flatItems.size()) {
            CartItem itemToDel = flatItems.get(delChoice);
            String depName = flatDepNames.get(delChoice);
            LinkedHashMap<String, CartItem> depCart = cart.get(depName);
            
            if (itemToDel.quantity > 1) {
                int qToDel = readInt("У вас " + itemToDel.quantity + " шт. цієї послуги. Скільки штук видалити? ");
                if (qToDel >= itemToDel.quantity) {
                    depCart.remove(itemToDel.serviceName);
                    System.out.println("Позицію повністю видалено.");
                } else if (qToDel > 0) {
                    itemToDel.quantity -= qToDel;
                    System.out.println("Кількість зменшено на " + qToDel + " шт.");
                } else {
                    System.out.println("Скасовано.");
                }
            } else {
                depCart.remove(itemToDel.serviceName);
                System.out.println("Позицію видалено.");
            }
            
            // Якщо відділення стало порожнім - видаляємо його з кошика
            if (depCart.isEmpty()) {
                cart.remove(depName);
            }
        } else {
            System.out.println("Некоректний вибір.");
        }
        pause();
    }

    // Метод запису багатопозиційного чека у консоль та файл
    private void generateReceipt(String client, LinkedHashMap<String, LinkedHashMap<String, CartItem>> cart, int total) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("=========================================\n");
        receipt.append("          ЧЕК АВТОСЕРВІСУ                \n");
        receipt.append("=========================================\n");
        receipt.append("Клієнт: ").append(client).append("\n\n");
        receipt.append("Деталізація замовлення:\n");
        
        for (Map.Entry<String, LinkedHashMap<String, CartItem>> entry : cart.entrySet()) {
            receipt.append("Відділення [").append(entry.getKey()).append("]:\n");
            for (CartItem item : entry.getValue().values()) {
                int rowTotal = item.unitPrice * item.quantity;
                if (item.quantity > 1) {
                    receipt.append(String.format("  - %-25s x%-2d ...... %4d грн (по %d грн)\n", item.serviceName, item.quantity, rowTotal, item.unitPrice));
                } else {
                    receipt.append(String.format("  - %-25s    ...... %4d грн\n", item.serviceName, rowTotal));
                }
            }
        }
        
        receipt.append("-----------------------------------------\n");
        receipt.append("ДО СПЛАТИ: ").append(total).append(" грн\n");
        receipt.append("=========================================\n");

        System.out.print(receipt.toString());
        
        try (FileWriter writer = new FileWriter("receipt_" + client + ".txt", true)) {
            writer.write(receipt.toString() + "\n");
            System.out.println("\n(Чек успішно збережено у файл receipt_" + client + ".txt)");
        } catch (IOException e) {
            System.out.println("\nПомилка при збереженні чека у файл: " + e.getMessage());
        }
    }

    // ================= СЕРІАЛІЗАЦІЯ ДАНИХ =================
    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(auto);
            System.out.println("Дані автосервісу успішно збережено у базу.");
        } catch (IOException e) {
            System.out.println("Помилка при збереженні даних: " + e.getMessage());
        }
    }

    private void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                auto = (AutoService) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Помилка завантаження даних. Створено нову базу.");
                auto = new AutoService("АвтоПлюс");
            }
        } else {
            auto = new AutoService("АвтоПлюс");
        }
    }
}

public class Kurs_Auto {
    public static void main(String[] args) {
        InterfaceManager app = new InterfaceManager();
        app.start();
    }
}
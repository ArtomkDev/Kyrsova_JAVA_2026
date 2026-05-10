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
                    System.out.print("Назва послуги: ");
                    String sName = scanner.nextLine();
                    int sPrice = readInt("Ціна (грн): ");
                    dep.addService(sName, sPrice);
                    System.out.println("Послугу додано.");
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
                        
                        int newPrice = readInt("Нова ціна (грн): ");
                        
                        dep.removeService(oldName);
                        dep.addService(newName, newPrice);
                        System.out.println("Послугу успішно оновлено.");
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

        // Кошик клієнта: Ключ - назва відділення, Значення - список обраних послуг
        LinkedHashMap<String, List<String>> cart = new LinkedHashMap<>();
        int totalSum = 0;
        int itemsCount = 0;
        boolean ordering = true;

        while (ordering) {
            clearConsole();
            System.out.println("=== ВАШ КОШИК (" + itemsCount + " позицій на суму " + totalSum + " грн) ===");
            if (cart.isEmpty()) {
                System.out.println("  [Кошик порожній]");
            } else {
                for (Map.Entry<String, List<String>> entry : cart.entrySet()) {
                    System.out.println("Відділення [" + entry.getKey() + "]:");
                    for (String s : entry.getValue()) {
                        System.out.println("  - " + s);
                    }
                }
            }
            System.out.println("=========================================");

            System.out.println("\nОберіть відділення для замовлення послуги:");
            for (int i = 0; i < deps.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + deps.get(i).getName());
            }
            System.out.println("\n[0] Завершити замовлення та отримати чек");

            int depChoice = readInt("Ваш вибір: ");
            
            if (depChoice == 0) {
                ordering = false; 
                break;
            }

            depChoice -= 1; 
            if (depChoice < 0 || depChoice >= deps.size()) {
                System.out.println("Некоректний вибір.");
                pause();
                continue;
            }

            ServiceDepartment chosenDep = deps.get(depChoice);
            LinkedHashMap<String, Integer> services = chosenDep.getPriceList();
            
            if (services.isEmpty()) {
                System.out.println("У цьому відділенні наразі немає послуг.");
                pause();
                continue;
            }

            boolean choosingService = true;
            while (choosingService) {
                clearConsole();
                System.out.println("--- Послуги відділення: " + chosenDep.getName() + " ---");
                List<String> serviceKeys = new ArrayList<>(services.keySet());
                for (int i = 0; i < serviceKeys.size(); i++) {
                    System.out.println("[" + (i + 1) + "] " + serviceKeys.get(i) + " - " + services.get(serviceKeys.get(i)) + " грн");
                }
                System.out.println("\n[0] Повернутися до вибору відділень");

                int sChoice = readInt("Ваш вибір: ") - 1;
                
                if (sChoice == -1) {
                    choosingService = false; 
                } else if (sChoice >= 0 && sChoice < serviceKeys.size()) {
                    String selectedService = serviceKeys.get(sChoice);
                    int price = services.get(selectedService);
                    
                    // Перевірка на дублікати
                    List<String> depCart = cart.getOrDefault(chosenDep.getName(), new ArrayList<>());
                    if (depCart.contains(selectedService)) {
                        System.out.println("\nУВАГА! Ви вже додали цю послугу до кошика.");
                        System.out.println("Ви впевнені, що хочете додати її ще раз? При додатковій однаковій послузі ДОДАТКОВО ВИ НІЧОГО НЕ ОТРИМАЄТЕ.");
                        int confirm = readInt("1 - Так, додати / 0 - Скасувати: ");
                        if (confirm != 1) {
                            System.out.println("Додавання скасовано.");
                            pause();
                            continue; 
                        }
                    }
                    
                    // Додавання до кошика
                    depCart.add(selectedService);
                    cart.put(chosenDep.getName(), depCart);
                    totalSum += price;
                    itemsCount++;
                    
                    System.out.println("\nПослугу '" + selectedService + "' успішно додано до замовлення!");
                    pause();
                    choosingService = false; 
                } else {
                    System.out.println("Некоректний вибір послуги.");
                    pause();
                }
            }
        }

        if (!cart.isEmpty()) {
            clearConsole();
            generateReceipt(clientName, cart, totalSum);
        } else {
            System.out.println("\nВи нічого не замовили. Скасування.");
        }
        pause();
    }

    // Метод запису багатопозиційного чека у консоль та файл
    private void generateReceipt(String client, LinkedHashMap<String, List<String>> cart, int total) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("=======================\n");
        receipt.append("   ЧЕК АВТОСЕРВІСУ     \n");
        receipt.append("=======================\n");
        receipt.append("Клієнт: ").append(client).append("\n\n");
        receipt.append("Замовлені послуги:\n");
        
        for (Map.Entry<String, List<String>> entry : cart.entrySet()) {
            receipt.append("Відділення [").append(entry.getKey()).append("]:\n");
            for (String s : entry.getValue()) {
                receipt.append("  - ").append(s).append("\n");
            }
        }
        
        receipt.append("-----------------------\n");
        receipt.append("ДО СПЛАТИ: ").append(total).append(" грн\n");
        receipt.append("=======================\n");

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
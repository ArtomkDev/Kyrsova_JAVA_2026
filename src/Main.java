import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Базовий клас відділу обслуговування.
 * Інкапсулює дані про назву відділу та його прайс-лист.
 */
class ServiceDepartment {
    private String name;
    
    // Використовуємо LinkedHashMap для збереження порядку додавання послуг
    private LinkedHashMap<String, Integer> priceList = new LinkedHashMap<>();

    public ServiceDepartment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Додає одну послугу до прайс-листа.
     * @param service назва послуги
     * @param cost ціна послуги
     */
    public void addService(String service, int cost) {
        priceList.put(service, cost);
    }

    /**
     * Отримує прайс-лист відділу.
     * @return словник прайс-листа (назва послуги -> ціна)
     */
    public LinkedHashMap<String, Integer> getPriceList() {
        return priceList;
    }
}

/**
 * Клас автосервісу.
 * Містить список відділів (демонструє принцип композиції/агрегації, а не спадкування).
 */
class AutoService {
    private String name;
    private ArrayList<ServiceDepartment> departments = new ArrayList<>();

    public AutoService(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addDepartment(ServiceDepartment department) {
        departments.add(department);
    }

    public ArrayList<ServiceDepartment> getDepartments() {
        return departments;
    }

    /**
     * Заповнює автосервіс тестовими даними (для режиму Клієнта).
     */
    public void initializeDefaultData() {
        ServiceDepartment dep1 = new ServiceDepartment("Шиномонтаж");
        dep1.addService("Балансування коліс", 200);
        dep1.addService("Заміна шини", 150);
        dep1.addService("Ремонт проколу", 300);
        this.addDepartment(dep1);

        ServiceDepartment dep2 = new ServiceDepartment("Діагностика");
        dep2.addService("Діагностика ходової", 400);
        dep2.addService("Комп'ютерна діагностика", 500);
        dep2.addService("Перевірка рідин", 100);
        this.addDepartment(dep2);

        ServiceDepartment dep3 = new ServiceDepartment("Моторист");
        dep3.addService("Заміна масла", 300);
        dep3.addService("Капітальний ремонт двигуна", 15000);
        dep3.addService("Заміна ГРМ", 2500);
        this.addDepartment(dep3);
    }
}

/**
 * Клас для зберігання інформації про замовлення конкретного клієнта.
 * Розділяє логіку автосервісу та стан сесії користувача.
 */
class Order {
    private String clientName;
    private ServiceDepartment chosenDepartment;
    private String chosenServiceName;
    private int chosenServicePrice;

    public Order(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setChosenDepartment(ServiceDepartment chosenDepartment) {
        this.chosenDepartment = chosenDepartment;
    }

    public ServiceDepartment getChosenDepartment() {
        return chosenDepartment;
    }

    public void setChosenService(String name, int price) {
        this.chosenServiceName = name;
        this.chosenServicePrice = price;
    }

    public String getChosenServiceName() {
        return chosenServiceName;
    }

    public int getChosenServicePrice() {
        return chosenServicePrice;
    }
}

/**
 * Клас-контролер програми, який містить логіку взаємодії з користувачем.
 */
class AppController {
    private static ArrayList<String> infoManager = new ArrayList<>();
    private static ArrayList<String> infoClient = new ArrayList<>();
    
    public static int selectRole() {
        int role = 0;
        while (true) {
            System.out.println("\nОберіть вашу роль:");
            System.out.println("1 - Клієнт");
            System.out.println("2 - Менеджер");
            System.out.print("Ваш вибір (1 або 2): ");
            try {
                role = Main.scanner.nextInt();
                Main.scanner.nextLine(); 
                
                if (role == 1 || role == 2) {
                    break;
                } else {
                    System.out.println("Невірний вибір. Оберіть 1 або 2.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Помилка: введено не число! Будь ласка, введіть 1 або 2.");
                Main.scanner.nextLine(); 
            }
        }
        return role;
    }

    public static void runManagerMode() {
        System.out.println("\n--- РЕЖИМ МЕНЕДЖЕРА ---");
        System.out.print("Введіть назву нового автосервісу: ");
        String shopName = Main.scanner.nextLine();
        
        AutoService autoService = new AutoService(shopName);
        
        int numOfDep = 0;
        while (true) {
            System.out.print("Введіть кількість відділів (більше нуля): ");
            try {
                numOfDep = Main.scanner.nextInt();
                Main.scanner.nextLine();
                if (numOfDep > 0) {
                    break;
                } else {
                    System.out.println("Кількість відділів повинна бути більшою за 0.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Помилка: потрібно ввести ціле число!");
                Main.scanner.nextLine(); 
            }
        }
        
        // Наповнення відділів та прайс-листів
        for (int i = 0; i < numOfDep; i++) {
            System.out.print("\nВведіть назву " + (i + 1) + "-го відділу: ");
            String depName = Main.scanner.nextLine();
            ServiceDepartment dep = new ServiceDepartment(depName);
            
            System.out.println("--- Наповнення прайс-листа для '" + depName + "' ---");
            while (true) {
                System.out.print("Введіть назву послуги (або 'stop' / 'стоп' для завершення): ");
                String serviceName = Main.scanner.nextLine();
                
                // Перевірка на ключове слово зупинки (англійською та українською)
                if (serviceName.equalsIgnoreCase("стоп") || serviceName.equalsIgnoreCase("stop")) {
                    break;
                }
                
                int price = 0;
                while (true) {
                    System.out.print("Введіть ціну послуги '" + serviceName + "': ");
                    try {
                        price = Main.scanner.nextInt();
                        Main.scanner.nextLine();
                        if(price >= 0) break;
                        else System.out.println("Ціна не може бути від'ємною.");
                    } catch (InputMismatchException e) {
                        System.out.println("Помилка: введіть коректну ціну (число).");
                        Main.scanner.nextLine();
                    }
                }
                dep.addService(serviceName, price);
            }
            autoService.addDepartment(dep);
        }
        
        saveManagerReport(autoService);
        displayInfo(infoManager);
    }

    public static void runClientMode() {
        AutoService autoService = new AutoService("СТО АвтоПлюс");
        autoService.initializeDefaultData();
        
        System.out.println("\n--- РЕЖИМ КЛІЄНТА ---");
        System.out.println("Вітаємо в автосервісі '" + autoService.getName() + "'!");
        
        System.out.print("Будь ласка, введіть ваше ім'я: ");
        String clientName = Main.scanner.nextLine();
        Order currentOrder = new Order(clientName);
        
        // Вибір відділу
        ArrayList<ServiceDepartment> departments = autoService.getDepartments();
        while (true) {
            System.out.println("\nОберіть відділ (введіть номер):");
            for (int i = 0; i < departments.size(); i++) {
                System.out.println((i + 1) + ". " + departments.get(i).getName());
            }
            System.out.print("Ваш вибір: ");
            try {
                int choice = Main.scanner.nextInt();
                Main.scanner.nextLine();
                
                if (choice >= 1 && choice <= departments.size()) {
                    currentOrder.setChosenDepartment(departments.get(choice - 1));
                    break;
                } else {
                    System.out.println("Невірний вибір. Такого відділу не існує.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Помилка: потрібно ввести число!");
                Main.scanner.nextLine();
            }
        }
        
        // Вибір послуги
        ServiceDepartment chosenDep = currentOrder.getChosenDepartment();
        LinkedHashMap<String, Integer> priceList = chosenDep.getPriceList();
        ArrayList<String> serviceNames = new ArrayList<>(priceList.keySet());
        
        while (true) {
            System.out.println("\nПрайс-лист відділу '" + chosenDep.getName() + "'. Оберіть послугу:");
            for (int i = 0; i < serviceNames.size(); i++) {
                String sName = serviceNames.get(i);
                System.out.println((i + 1) + ". " + sName + " - " + priceList.get(sName) + " грн");
            }
            System.out.print("Ваш вибір: ");
            try {
                int choice = Main.scanner.nextInt();
                Main.scanner.nextLine();
                
                if (choice >= 1 && choice <= serviceNames.size()) {
                    String sName = serviceNames.get(choice - 1);
                    int sPrice = priceList.get(sName);
                    currentOrder.setChosenService(sName, sPrice);
                    break;
                } else {
                    System.out.println("Невірний вибір. Такої послуги не існує.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Помилка: потрібно ввести число!");
                Main.scanner.nextLine();
            }
        }
        
        saveClientReceipt(autoService, currentOrder);
        displayInfo(infoClient);
    }

    private static void saveManagerReport(AutoService autoService) {
        StringBuilder report = new StringBuilder();
        report.append("\n=== ЗВІТ ПРО СТВОРЕННЯ АВТОСЕРВІСУ ===\n");
        report.append("Назва: ").append(autoService.getName()).append("\n");
        report.append("Кількість відділів: ").append(autoService.getDepartments().size()).append("\n");
        report.append("Список відділів та послуг:\n");
        
        for (int i = 0; i < autoService.getDepartments().size(); i++) {
            ServiceDepartment dep = autoService.getDepartments().get(i);
            report.append("  ").append(i + 1).append(". ").append(dep.getName()).append("\n");
            for (String service : dep.getPriceList().keySet()) {
                report.append("     - ").append(service).append(": ").append(dep.getPriceList().get(service)).append(" грн\n");
            }
        }
        report.append("======================================\n");
        infoManager.add(report.toString());
    }

    private static void saveClientReceipt(AutoService autoService, Order order) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("\n=== ФІНАЛЬНИЙ ЧЕК КЛІЄНТА ===\n");
        receipt.append("Автосервіс: ").append(autoService.getName()).append("\n");
        receipt.append("Клієнт: ").append(order.getClientName()).append("\n");
        receipt.append("Відділ: ").append(order.getChosenDepartment().getName()).append("\n");
        receipt.append("Замовлена послуга: ").append(order.getChosenServiceName()).append("\n");
        receipt.append("До сплати: ").append(order.getChosenServicePrice()).append(" грн\n");
        receipt.append("Дякуємо за візит!\n");
        receipt.append("=============================\n");
        infoClient.add(receipt.toString());
    }

    private static void displayInfo(ArrayList<String> info) {
        for (String record : info) {
            System.out.println(record);
        }
    }
}

/**
 * Головний клас програми (Entry Point).
 */
public class Main {
    // Використовуємо стандартний Scanner, який адаптується до кодування консолі
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int userRole = AppController.selectRole();
        
        if (userRole == 1) {
            AppController.runClientMode();
        } else if (userRole == 2) {
            AppController.runManagerMode();
        }
        
        scanner.close(); // Закриваємо сканер по завершенню роботи
    }
}
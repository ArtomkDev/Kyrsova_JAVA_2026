import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Базовий клас відділу обслуговування.
 * Інкапсулює дані про назву відділу та його прайс-лист.
 */
class Service_Department {
    /** Назва відділу */
    private String name_dep;
    
    /** Прайс-лист відділу: назва послуги -> ціна */
    private HashMap<String, Integer> price = new HashMap<>();

    /**
     * Конструктор за замовчуванням.
     */
    Service_Department() {
    }

    /**
     * Конструктор з параметром для встановлення назви відділу.
     * @param name Назва відділу
     */
    Service_Department(String name) {
        this.name_dep = name;
    }

    /**
     * Отримує назву відділу.
     * @return назва відділу
     */
    protected String getName() {
        return name_dep;
    }

    /**
     * Встановлює назву відділу.
     * @param name назва відділу
     */
    protected void setNameDep(String name) {
        this.name_dep = name;
    }

    /**
     * Заповнює прайс-лист на основі масивів послуг та цін.
     * @param service масив назв послуг
     * @param cost масив цін
     */
    protected void setPrice(String[] service, int[] cost) {
        for (int i = 0; i < service.length; i++) {
            price.put(service[i], cost[i]);
        }
    }

    /**
     * Отримує прайс-лист відділу.
     * @return словник прайс-листа (назва послуги -> ціна)
     */
    protected HashMap<String, Integer> getPrice() {
        return price;
    }
}

/**
 * Клас автосервісу, що успадковує властивості базового відділу обслуговування.
 * Зберігає інформацію про весь автосервіс, колекцію відділів та вибір клієнта.
 */
class Auto_Service extends Service_Department {
    /** Назва автосервісу */
    private String name;
    
    /** Кількість відділів */
    private int num_of_dep;
    
    /** Список відділів в автосервісі */
    protected ArrayList<Service_Department> departments = new ArrayList<>();
    
    /** Ім'я клієнта */
    private String client_name;
    
    /** Вибір відділу клієнтом (збереження індексу) */
    private int choice_dep;
    
    /** Вибір послуги клієнтом (збереження індексу) */
    private int choice_service;

    /** Назва обраної послуги */
    private String chosen_service_name;
    
    /** Ціна обраної послуги */
    private int chosen_service_price;

    /**
     * Конструктор за замовчуванням для ролі Клієнта.
     * Містить жорстко задані (hardcoded) дані про автосервіс.
     */
    Auto_Service() {
        this.name = "СТО АвтоПлюс";
        this.num_of_dep = 3;
        
        // Створення 1-го відділу
        Service_Department dep1 = new Service_Department("Шиномонтаж");
        dep1.setPrice(
            new String[]{"Балансування коліс", "Заміна шини", "Ремонт проколу"}, 
            new int[]{200, 150, 300}
        );
        departments.add(dep1);
        
        // Створення 2-го відділу
        Service_Department dep2 = new Service_Department("Діагностика");
        dep2.setPrice(
            new String[]{"Діагностика ходової", "Комп'ютерна діагностика", "Перевірка рідин"}, 
            new int[]{400, 500, 100}
        );
        departments.add(dep2);
        
        // Створення 3-го відділу
        Service_Department dep3 = new Service_Department("Моторист");
        dep3.setPrice(
            new String[]{"Заміна масла", "Капітальний ремонт двигуна", "Заміна ГРМ"}, 
            new int[]{300, 15000, 2500}
        );
        departments.add(dep3);
    }

    /**
     * Конструктор з параметрами для ролі Менеджера.
     * @param n Назва автосервісу
     * @param num Кількість відділів
     */
    Auto_Service(String n, int num) {
        this.name = n;
        this.num_of_dep = num;
    }

    /**
     * Метод для менеджера: зчитує назви відділів з консолі у циклі та створює їх.
     */
    protected void setService_Departments() {
        for (int i = 0; i < num_of_dep; i++) {
            System.out.print("Введіть назву " + (i + 1) + "-го відділу: ");
            String depName = Main.scanner.nextLine();
            Service_Department dep = new Service_Department(depName);
            departments.add(dep);
        }
    }

    /**
     * Метод для клієнта: виводить меню відділів та безпечно обробляє вибір.
     */
    protected void setChoiceDep() {
        while (true) {
            System.out.println("\nОберіть відділ (введіть номер):");
            for (int i = 0; i < departments.size(); i++) {
                System.out.println((i + 1) + ". " + departments.get(i).getName());
            }
            System.out.print("Ваш вибір: ");
            try {
                int choice = Main.scanner.nextInt();
                Main.scanner.nextLine(); // Очищення символу нового рядка після nextInt()
                
                if (choice >= 1 && choice <= departments.size()) {
                    this.choice_dep = choice - 1;
                    break;
                } else {
                    System.out.println("Невірний вибір. Такого відділу не існує.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Помилка: потрібно ввести число!");
                Main.scanner.nextLine(); // Очищення невірного вводу
            }
        }
    }

    /**
     * Метод для клієнта: виводить прайс-лист обраного відділу (з HashMap) та обробляє вибір послуги.
     */
    protected void setServiceChoice() {
        Service_Department chosenDep = departments.get(this.choice_dep);
        HashMap<String, Integer> priceList = chosenDep.getPrice();
        
        // Зберігаємо ключі HashMap у ArrayList для забезпечення стабільного порядку індексів меню
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
                Main.scanner.nextLine(); // Очищення символу нового рядка
                
                if (choice >= 1 && choice <= serviceNames.size()) {
                    this.choice_service = choice - 1;
                    this.chosen_service_name = serviceNames.get(this.choice_service);
                    this.chosen_service_price = priceList.get(this.chosen_service_name);
                    break;
                } else {
                    System.out.println("Невірний вибір. Такої послуги не існує.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Помилка: потрібно ввести число!");
                Main.scanner.nextLine(); // Очищення невірного вводу
            }
        }
    }

    /**
     * Отримує назву автосервісу.
     * @return назва автосервісу
     */
    protected String getShopName() {
        return name;
    }

    /**
     * Отримує кількість відділів автосервісу.
     * @return кількість відділів
     */
    protected int getNumOfDep() {
        return num_of_dep;
    }

    protected String getClient_name() {
        return client_name;
    }

    protected void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    protected String getChosenServiceName() {
        return chosen_service_name;
    }

    protected int getChosenServicePrice() {
        return chosen_service_price;
    }
}

/**
 * Клас-контролер програми, який містить логіку взаємодії з користувачем.
 */
class TestService {
    /** Колекція для збереження історії дій менеджера */
    protected static ArrayList<String> info_manager = new ArrayList<>();
    
    /** Колекція для збереження чеків клієнтів */
    protected static ArrayList<String> info_client = new ArrayList<>();
    
    /** Глобальний об'єкт автосервісу */
    protected static Auto_Service autoService;

    /**
     * Виводить меню вибору ролі (Клієнт або Менеджер) та безпечно обробляє вибір.
     * @return 1 (Клієнт) або 2 (Менеджер)
     */
    protected static int role() {
        int role = 0;
        while (true) {
            System.out.println("\nОберіть вашу роль:");
            System.out.println("1 - Клієнт");
            System.out.println("2 - Менеджер");
            System.out.print("Ваш вибір (1 або 2): ");
            try {
                role = Main.scanner.nextInt();
                Main.scanner.nextLine(); // Очищення символу нового рядка
                
                if (role == 1 || role == 2) {
                    break;
                } else {
                    System.out.println("Невірний вибір. Оберіть 1 або 2.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Помилка: введено не число! Будь ласка, введіть 1 або 2.");
                Main.scanner.nextLine(); // Очищення невірного вводу
            }
        }
        return role;
    }

    /**
     * Запускає логіку для режиму "Менеджер".
     */
    protected static void testing_manager() {
        System.out.println("\n--- РЕЖИМ МЕНЕДЖЕРА ---");
        System.out.print("Введіть назву нового автосервісу: ");
        String shopName = Main.scanner.nextLine();
        
        int numOfDep = 0;
        while (true) {
            System.out.print("Введіть кількість відділів (більше нуля): ");
            try {
                numOfDep = Main.scanner.nextInt();
                Main.scanner.nextLine(); // Очищення буфера
                if (numOfDep > 0) {
                    break;
                } else {
                    System.out.println("Кількість відділів повинна бути більшою за 0.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Помилка: потрібно ввести ціле число!");
                Main.scanner.nextLine(); // Очищення буфера
            }
        }
        
        // Створення об'єкта автосервісу з параметрами менеджера
        autoService = new Auto_Service(shopName, numOfDep);
        autoService.setService_Departments();
        
        // Збереження та виведення звіту
        InputDataManager();
        Demonst(info_manager);
    }

    /**
     * Запускає логіку для режиму "Клієнт".
     */
    protected static void testing_client() {
        // Створення об'єкта автосервісу з жорстко заданими даними
        autoService = new Auto_Service();
        System.out.println("\n--- РЕЖИМ КЛІЄНТА ---");
        System.out.println("Вітаємо в автосервісі '" + autoService.getShopName() + "'!");
        
        System.out.print("Будь ласка, введіть ваше ім'я: ");
        String cName = Main.scanner.nextLine();
        autoService.setClient_name(cName);
        
        // Виклик методів вибору відділу та послуги
        autoService.setChoiceDep();
        autoService.setServiceChoice();
        
        // Формування, збереження та виведення фінального чеку
        DataInputClient();
        Demonst(info_client);
    }

    /**
     * Форматує дані, введені менеджером, та додає їх у відповідний ArrayList.
     */
    protected static void InputDataManager() {
        StringBuilder report = new StringBuilder();
        report.append("\n=== ЗВІТ ПРО СТВОРЕННЯ АВТОСЕРВІСУ ===\n");
        report.append("Назва: ").append(autoService.getShopName()).append("\n");
        report.append("Кількість відділів: ").append(autoService.getNumOfDep()).append("\n");
        report.append("Список відділів:\n");
        for (int i = 0; i < autoService.departments.size(); i++) {
            report.append(i + 1).append(". ").append(autoService.departments.get(i).getName()).append("\n");
        }
        report.append("======================================\n");
        info_manager.add(report.toString());
    }

    /**
     * Форматує чек клієнта та додає його у відповідний ArrayList.
     */
    protected static void DataInputClient() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("\n=== ФІНАЛЬНИЙ ЧЕК КЛІЄНТА ===\n");
        receipt.append("Автосервіс: ").append(autoService.getShopName()).append("\n");
        receipt.append("Клієнт: ").append(autoService.getClient_name()).append("\n");
        receipt.append("Замовлена послуга: ").append(autoService.getChosenServiceName()).append("\n");
        receipt.append("До сплати: ").append(autoService.getChosenServicePrice()).append(" грн\n");
        receipt.append("Дякуємо за візит!\n");
        receipt.append("=============================\n");
        info_client.add(receipt.toString());
    }

    /**
     * Ітерується по заданій колекції та виводить інформацію на екран.
     * @param info колекція зі звітами (info_manager або info_client)
     */
    protected static void Demonst(ArrayList<String> info) {
        for (String record : info) {
            System.out.println(record);
        }
    }
}

/**
 * Головний клас програми (Entry Point).
 */
public class Main {
    /** Глобальний сканер для вводу з консолі з підтримкою UTF-8 */
    public static final Scanner scanner = new Scanner(System.in, "UTF-8");

    /**
     * Точка входу в програму.
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        int userRole = TestService.role();
        
        if (userRole == 1) {
            TestService.testing_client();
        } else if (userRole == 2) {
            TestService.testing_manager();
        }
    }
}

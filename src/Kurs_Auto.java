package kurs_auto;

import java.util.*;

class ServiceDepartment {
    private String name_dep; // назва відділення
    private LinkedHashMap<String, Integer> price = new LinkedHashMap<>(); // прайс цін

    ServiceDepartment() {
    }

    ServiceDepartment(String name) {
        name_dep = name;
    }

    protected String getName() { // повернути назву відділення
        return name_dep;
    }

    protected void setNameDep(String name) { // встановити назву відділення
        name_dep = name;
    }

    protected void setPrice(String[] service, int[] cost) { // встановлення прайсу
        price.clear();
        if (service.length == cost.length) {
            for (int i = 0; i < service.length; i++) {
                price.put(service[i], cost[i]);
            }
        }
    }

    protected LinkedHashMap<String, Integer> getPrice() { // повертає прайс цін та послуг
        return price;
    }
}

class AutoService extends ServiceDepartment {
    private String name; // назва автосервісу
    private int num_of_dep; // к-сть відділень
    protected ArrayList<ServiceDepartment> departments = new ArrayList<>(); // список відділень, які є в автосервісі
    private String client_name; // ім'я клієнта
    private int choicedep; // вибір відділення
    private int choiceservice; // вибір послуги

    // Клієнт
    AutoService() { // по замовчуванню (шаблон для клієнта)
        departments.clear();
        name = "АвтоПлюс";
        String[] dep = { "Шиномонтаж", "Діагностика", "Моторист" };
        num_of_dep = dep.length;
        for (int i = 0; i < num_of_dep; i++) {
            departments.add(new ServiceDepartment(dep[i]));
        }

        String[] shin = { "Балансування коліс", "Заміна шини", "Ремонт проколу" };
        int[] shinP = { 200, 150, 300 };
        departments.get(0).setPrice(shin, shinP);

        String[] diag = { "Діагностика ходової", "Комп'ютерна діагностика", "Перевірка рідин" };
        int[] diagP = { 400, 500, 100 };
        departments.get(1).setPrice(diag, diagP);

        String[] mot = { "Заміна масла", "Капітальний ремонт двигуна", "Заміна ГРМ" };
        int[] motP = { 300, 15000, 2500 };
        departments.get(2).setPrice(mot, motP);
    }

    // Менеджер
    AutoService(String n, int num) {
        name = n;
        num_of_dep = num;
    }

    protected int getChoiceDep() {
        return choicedep;
    }

    protected String getStrChoiceService() { // повертає обрану послугу
        LinkedHashMap<String, Integer> price_dep = departments.get(choicedep - 1).getPrice();
        int a = 0;
        String str = "";
        for (Map.Entry<String, Integer> entry : price_dep.entrySet()) {
            if (a > choiceservice - 1)
                break;
            else {
                str = entry.getKey();
            }
            a += 1;
        }
        return str;
    }

    protected int getPrChoiceService() { // повертає ціну на обрану послугу
        LinkedHashMap<String, Integer> price_dep = departments.get(choicedep - 1).getPrice();
        int a = 0, cost = 0;
        for (Map.Entry<String, Integer> entry : price_dep.entrySet()) {
            if (a > choiceservice - 1)
                break;
            else {
                cost = entry.getValue();
            }
            a += 1;
        }
        return cost;
    }

    protected int getChoiceService() {
        return choiceservice;
    }

    protected void setChoiceDep() { // вибір відділення
        Scanner in = new Scanner(System.in, "UTF-8");
        System.out.println("\nШановний(а) " + client_name + ", оберіть відділення:");
        for (int i = 0; i < departments.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + departments.get(i).getName());
        }
        System.out.print("Ваш вибір (введіть цифру): ");
        int choice = in.nextInt();
        choicedep = 0;
        if (choice < 1 || choice > departments.size()) {
            System.out.println("Некоректний вибір! Спробуйте ще раз.");
            setChoiceDep();
        } else {
            choicedep = choice;
        }
    }

    protected void setServiceChoice() { // вибір послуги
        Scanner in = new Scanner(System.in, "UTF-8");
        System.out.println("\nОберіть послугу:");
        LinkedHashMap<String, Integer> price_dep = departments.get(choicedep - 1).getPrice();
        int i = 1;
        for (Map.Entry<String, Integer> entry : price_dep.entrySet()) {
            System.out.println("  [" + i + "] " + entry.getKey() + " ........... " + entry.getValue() + " грн");
            i++;
        }
        System.out.print("Ваш вибір (введіть цифру): ");
        int choice = in.nextInt();
        choiceservice = 0;
        if (choice < 1 || choice > price_dep.size()) {
            System.out.println("Некоректний вибір! Спробуйте ще раз.");
            setServiceChoice();
        } else {
            choiceservice = choice;
        }
    }

    protected void setNameClient(String name) {
        client_name = name;
    }

    protected String getNameClient() {
        return client_name;
    }

    protected int getNumofDep() {
        return num_of_dep;
    }

    protected String getNameAuto() {
        return name;
    }

    protected void setService_Departments() { // створення нових відділень, відповідно їх к-сті
        departments.clear();
        System.out.println("\n===== НАЛАШТУВАННЯ ВІДДІЛЕНЬ =====");
        for (int i = 0; i < num_of_dep; i++) {
            departments.add(newDep(i + 1));
        }
    }

    private ServiceDepartment newDep(int index) { // створити нове відділення
        Scanner in = new Scanner(System.in, "UTF-8");
        System.out.println("\n--- Відділення [" + index + "] ---");
        System.out.print("Назва відділення: ");
        String named = in.nextLine();
        ServiceDepartment dep = new ServiceDepartment(named);

        System.out.print("Скільки послуг буде у відділенні '" + named + "'? ");
        int count = in.nextInt();
        in.nextLine();

        String[] services = new String[count];
        int[] prices = new int[count];

        for (int i = 0; i < count; i++) {
            System.out.print("   > Назва послуги " + (i + 1) + ": ");
            services[i] = in.nextLine();
            System.out.print("   > Ціна послуги " + (i + 1) + " (грн): ");
            prices[i] = in.nextInt();
            in.nextLine();
        }

        dep.setPrice(services, prices);
        return dep;
    }
}

class Test {
    protected static ArrayList<String> info_manager = new ArrayList<>(); // колекція для збереження внесених даних менеджера
    protected static ArrayList<String> info_client = new ArrayList<>(); // колекція для збереження внесених даних клієнта
    protected static AutoService auto = new AutoService(); // об'єкт для тестування

    protected static void testing_manager() { // режим менеджера
        Scanner in = new Scanner(System.in, "UTF-8");
        System.out.print("Введіть назву автосервісу: ");
        String rez = in.nextLine();
        System.out.print("Введіть к-сть відділень: ");
        int num = in.nextInt();
        auto = new AutoService(rez, num);
        auto.setService_Departments(); // встановити відділення
        InputDataManager(); // зберегти введені дані менеджера в колекції
        Demonst(info_manager); // вивести збережені дані
    }

    protected static void InputDataManager() { // заповнення колекції для ролі менеджера
        info_manager.clear();
        String[] info = { "Назва автосервісу: ", "\nКількість відділень: ", "\nСписок відділень та прайс: \n" };
        info_manager.add(info[0]);
        info_manager.add(auto.getNameAuto());
        info_manager.add(info[1]);
        info_manager.add(Integer.toString(auto.getNumofDep()));
        info_manager.add(info[2]);

        for (int i = 0; i < auto.departments.size(); i++) {
            info_manager.add(" -> " + auto.departments.get(i).getName() + ":\n");

            LinkedHashMap<String, Integer> priceList = auto.departments.get(i).getPrice();
            for (Map.Entry<String, Integer> entry : priceList.entrySet()) {
                info_manager.add("    - " + entry.getKey() + " (" + entry.getValue() + " грн)\n");
            }
        }
    }

    protected static void Demonst(ArrayList<String> info) { // виведення на екран заповненої колекції
        System.out.println("\n-----Демонстрація введених данних-----");
        System.out.println("------\t------\t------\t------\t------");
        for (int i = 0; i < info.size(); i++) {
            System.out.print(info.get(i));
        }
        System.out.println("\n------\t------\t------\t------\t------");
    }

    protected static void testing_client() { // режим клієнта
        Scanner in = new Scanner(System.in, "UTF-8");
        auto = new AutoService();
        System.out.println("Вас вітає автосервіс " + auto.getNameAuto());
        System.out.print("Назвіть будь ласка своє ім'я: ");
        String name = in.nextLine();
        auto.setNameClient(name);
        auto.setChoiceDep();
        auto.setServiceChoice();
        System.out.println("\nДякуємо за замовлення! Формуємо чек...");
        DataInputClient();
        Demonst(info_client);
    }

    protected static void DataInputClient() { // зберігає в колекції дані, введені клієнтом
        info_client.clear();
        String[] info = { "Ім'я клієнта: ", "\nВибір відділення: ", "\nВибір послуги: ", "\nСума розрахунку: " };
        info_client.add(info[0]);
        info_client.add(auto.getNameClient());
        info_client.add(info[1]);
        info_client.add((auto.departments.get(auto.getChoiceDep() - 1).getName()));
        info_client.add(info[2]);
        info_client.add(auto.getStrChoiceService());
        info_client.add(info[3]);
        info_client.add(Integer.toString(auto.getPrChoiceService()));
    }

    protected static int role() { // вибір режиму
        Scanner in = new Scanner(System.in);
        System.out.println("Оберіть роль для входу:\n 1 - Клієнт  2 - Менеджер");
        int role = in.nextInt(), rez = 0;
        if (role != 1 && role != 2) {
            System.out.println("Некоректний вибір!");
            rez = role();
        } else {
            rez = role;
        }
        return rez;
    }
}

public class Kurs_Auto {
    public static void main(String[] args) {
        int user = Test.role(); // вибір режиму користувача
        if (user == 2) {
            Test.testing_manager();
        } else {
            Test.testing_client();
        }
    }
}
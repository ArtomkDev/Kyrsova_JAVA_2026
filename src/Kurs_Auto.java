package kurs_auto;

import java.util.*;

class ServiceDepartment {
    private String name_dep;
    private LinkedHashMap<String, Integer> price = new LinkedHashMap<>();

    ServiceDepartment() {
    }

    ServiceDepartment(String name) {
        name_dep = name;
    }

    protected String getName() {
        return name_dep;
    }

    protected void setNameDep(String name) {
        name_dep = name;
    }

    protected void setPrice(String[] service, int[] cost) {
        price.clear();
        if (service.length == cost.length) {
            for (int i = 0; i < service.length; i++) {
                price.put(service[i], cost[i]);
            }
        }
    }

    protected LinkedHashMap<String, Integer> getPrice() {
        return price;
    }
}

class AutoService extends ServiceDepartment {
    private String name;
    private int num_of_dep;
    protected ArrayList<ServiceDepartment> departments = new ArrayList<>();
    private String client_name;
    private int choicedep;
    private int choiceservice;

    AutoService() {
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

    AutoService(String n, int num) {
        name = n;
        num_of_dep = num;
    }

    protected int getChoiceDep() {
        return choicedep;
    }

    protected String getStrChoiceService() {
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

    protected int getPrChoiceService() {
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

    protected void setChoiceDep() {
        Scanner in = new Scanner(System.in, "UTF-8");
        System.out.println(client_name + ", оберіть номер відділення, в якому хочете замовити послугу: ");
        for (int i = 0; i < departments.size(); i++) {
            System.out.println((i + 1) + " - " + departments.get(i).getName());
        }
        int choice = in.nextInt();
        choicedep = 0;
        if (choice < 1 || choice > departments.size()) {
            System.out.println("Uncorrect choice!!!");
            setChoiceDep();
        } else {
            choicedep = choice;
        }
    }

    protected void setServiceChoice() {
        Scanner in = new Scanner(System.in, "UTF-8");
        System.out.println(client_name + ", оберіть номер послуги, яку хочете замовити: ");
        LinkedHashMap<String, Integer> price_dep = departments.get(choicedep - 1).getPrice();
        int i = 1;
        for (Map.Entry<String, Integer> entry : price_dep.entrySet()) {
            System.out.println(i + " - " + entry.getKey() + " - " + entry.getValue());
            i++;
        }
        int choice = in.nextInt();
        choiceservice = 0;
        if (choice < 1 || choice > price_dep.size()) {
            System.out.println("Uncorrect choice!!!");
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

    protected void setService_Departments() {
        departments.clear();
        System.out.println("-----Визначаємо відділення-----");
        for (int i = 0; i < num_of_dep; i++) {
            departments.add(newDep());
        }
    }

    private ServiceDepartment newDep() {
        Scanner in = new Scanner(System.in, "UTF-8");
        System.out.print("Назва відділення : ");
        String named = in.nextLine();
        ServiceDepartment dep = new ServiceDepartment(named);

        System.out.print("Скільки послуг буде у відділенні '" + named + "'? ");
        int count = in.nextInt();
        in.nextLine();

        String[] services = new String[count];
        int[] prices = new int[count];

        for (int i = 0; i < count; i++) {
            System.out.print("Назва послуги " + (i + 1) + ": ");
            services[i] = in.nextLine();
            System.out.print("Ціна послуги " + (i + 1) + " (грн): ");
            prices[i] = in.nextInt();
            in.nextLine();
        }

        dep.setPrice(services, prices);
        return dep;
    }
}

class Test {
    protected static ArrayList<String> info_manager = new ArrayList<>();
    protected static ArrayList<String> info_client = new ArrayList<>();
    protected static AutoService auto = new AutoService();

    protected static void testing_manager() {
        Scanner in = new Scanner(System.in, "UTF-8");
        System.out.print("Введіть назву автосервісу : ");
        String rez = in.nextLine();
        System.out.print("Введіть к-сть відділень: ");
        int num = in.nextInt();
        auto = new AutoService(rez, num);
        auto.setService_Departments();
        InputDataManager();
        Demonst(info_manager);
    }

    protected static void InputDataManager() {
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

    protected static void Demonst(ArrayList<String> info) {
        System.out.println("-----Демонстрація введених данних-----");
        System.out.println("------\t------\t------\t------\t------");
        for (int i = 0; i < info.size(); i++) {
            System.out.print(info.get(i));
        }
        System.out.println("\n------\t------\t------\t------\t------");
    }

    protected static void testing_client() {
        Scanner in = new Scanner(System.in, "UTF-8");
        auto = new AutoService();
        System.out.println("Вас вітає автосервіс " + auto.getNameAuto());
        System.out.print("Назвіть будь ласка своє ім'я: ");
        String name = in.nextLine();
        auto.setNameClient(name);
        auto.setChoiceDep();
        auto.setServiceChoice();
        System.out.println("Дякуємо за замовлення!!!");
        DataInputClient();
        Demonst(info_client);
    }

    protected static void DataInputClient() {
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

    protected static int role() {
        Scanner in = new Scanner(System.in);
        System.out.println("Оберіть роль для входу:\n 1 - Клієнт 2 - Менеджер");
        int role = in.nextInt(), rez = 0;
        if (role != 1 && role != 2) {
            System.out.println("Uncorrect choice!!!");
            rez = role();
        } else {
            rez = role;
        }
        return rez;
    }
}

public class Kurs_Auto {
    public static void main(String[] args) {
        int user = Test.role();
        if (user == 2) {
            Test.testing_manager();
        } else {
            Test.testing_client();
        }
    }
}
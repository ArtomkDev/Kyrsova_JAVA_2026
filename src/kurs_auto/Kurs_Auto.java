package kurs_auto;

import java.io.*;
import java.util.*;

//  ІЄРАРХІЯ КЛАСІВ — БАЗОВІ АБСТРАКЦІЇ

/**
 * Абстрактний базовий клас для всіх іменованих сутностей системи.
 * <p>
 * Реалізує принцип єдиної відповідальності (SRP): зберігає назву
 * та оголошує контракт {@code displayInfo()}, який кожна підсутність
 * зобов'язана виконати по-своєму (поліморфізм).
 * </p>
 *
 * @author Студент
 * @version 2.0
 */
abstract class AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Назва сутності. Захищено від прямого зовнішнього доступу (інкапсуляція). */
    protected String name;

    /**
     * Конструктор базової сутності.
     *
     * @param name назва сутності; не може бути {@code null}
     */
    public AbstractEntity(String name) {
        this.name = name;
    }

    /**
     * Повертає назву сутності.
     *
     * @return назва
     */
    public String getName() {
        return name;
    }

    /**
     * Встановлює нову назву сутності.
     *
     * @param name нова назва
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Абстрактний метод відображення інформації.
     * <p>
     * Оголошений тут, щоб забезпечити <b>поліморфну</b> поведінку:
     * кожен нащадок реалізує власну логіку виводу без зміни коду,
     * що викликає метод.
     * </p>
     */
    public abstract void displayInfo();
}

//  ІЄРАРХІЯ КОРИСТУВАЧІВ (Person → Client / Manager)

/**
 * Абстрактний клас користувача системи.
 * <p>
 * Успадковує {@link AbstractEntity} і додає рольову семантику.
 * Є проміжним рівнем ієрархії між {@code AbstractEntity} та
 * конкретними ролями {@link Client} і {@link Manager}.
 * </p>
 */
abstract class Person extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    /**
     * Конструктор.
     *
     * @param name ім'я користувача
     */
    public Person(String name) {
        super(name);
    }
}

/**
 * Клас клієнта автосервісу.
 * <p>
 * Містить інкапсульований кошик замовлення ({@link Cart}).
 * Перевизначає {@code displayInfo()} для відображення
 * клієнтської ролі (поліморфізм).
 * </p>
 */
class Client extends Person {
    private static final long serialVersionUID = 1L;

    /** Кошик клієнта — повністю ізольований від зовнішнього коду. */
    private final Cart cart;

    /**
     * Конструктор клієнта.
     *
     * @param name ім'я клієнта
     */
    public Client(String name) {
        super(name);
        this.cart = new Cart();
    }

    /**
     * Повертає кошик клієнта.
     *
     * @return об'єкт {@link Cart}
     */
    public Cart getCart() {
        return cart;
    }

    /**
     * {@inheritDoc}
     * <p>Виводить повідомлення про початок клієнтського сеансу.</p>
     */
    @Override
    public void displayInfo() {
        System.out.println("Поточний сеанс: Клієнт [" + name + "]");
    }
}

/**
 * Клас менеджера автосервісу.
 * <p>
 * Перевизначає {@code displayInfo()} для відображення
 * менеджерської ролі (поліморфізм).
 * </p>
 */
class Manager extends Person {
    private static final long serialVersionUID = 1L;

    /**
     * Конструктор менеджера.
     *
     * @param name ім'я менеджера
     */
    public Manager(String name) {
        super(name);
    }

    /**
     * {@inheritDoc}
     * <p>Виводить повідомлення про початок менеджерського сеансу.</p>
     */
    @Override
    public void displayInfo() {
        System.out.println("Поточний сеанс: Менеджер автосервісу [" + name + "]");
    }
}

//  ІЄРАРХІЯ ВІДДІЛЕНЬ (AbstractDepartment → Repair / Diagnostic)

/**
 * Абстрактний базовий клас відділення автосервісу.
 * <p>
 * Успадковує {@link AbstractEntity}. Зберігає прайс-лист послуг
 * і забезпечує захищений доступ до нього через публічне API
 * (принцип інкапсуляції). Дочірні класи зобов'язані визначити
 * метод {@code displayInfo()} відповідно до свого типу.
 * </p>
 */
abstract class AbstractDepartment extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    /** Внутрішній прайс-лист: назва послуги → ціна (грн). */
    private final LinkedHashMap<String, Integer> priceList;

    /**
     * Конструктор відділення.
     *
     * @param name назва відділення
     */
    public AbstractDepartment(String name) {
        super(name);
        this.priceList = new LinkedHashMap<>();
    }

    /**
     * Додає або оновлює послугу у прайс-листі.
     *
     * @param serviceName назва послуги
     * @param cost        вартість у гривнях
     */
    public void addService(String serviceName, int cost) {
        priceList.put(serviceName, cost);
    }

    /**
     * Видаляє послугу з прайс-листа.
     *
     * @param serviceName назва послуги для видалення
     */
    public void removeService(String serviceName) {
        priceList.remove(serviceName);
    }

    /**
     * Перевіряє, чи існує послуга у прайс-листі.
     *
     * @param serviceName назва послуги
     * @return {@code true}, якщо послуга вже є
     */
    public boolean containsService(String serviceName) {
        return priceList.containsKey(serviceName);
    }

    /**
     * Повертає ціну послуги або {@code -1}, якщо послугу не знайдено.
     *
     * @param serviceName назва послуги
     * @return ціна у грн або {@code -1}
     */
    public int getServicePrice(String serviceName) {
        return priceList.getOrDefault(serviceName, -1);
    }

    /**
     * Повертає незмінний список назв послуг для ітерації
     * без ризику зовнішніх модифікацій внутрішнього стану.
     *
     * @return список назв послуг
     */
    public List<String> getServiceNames() {
        return new ArrayList<>(priceList.keySet());
    }

    /**
     * Повертає кількість послуг у прайс-листі.
     *
     * @return кількість послуг
     */
    public int getServicesCount() {
        return priceList.size();
    }

    /**
     * Перевіряє, чи прайс-лист порожній.
     *
     * @return {@code true}, якщо послуг немає
     */
    public boolean isServicesEmpty() {
        return priceList.isEmpty();
    }

    /**
     * Повертає захисну копію прайс-листа лише для читання.
     * Зовнішній код не може напряму змінити внутрішній стан.
     *
     * @return незмінна копія прайс-листа
     */
    public Map<String, Integer> getPriceListCopy() {
        return Collections.unmodifiableMap(priceList);
    }
}

/**
 * Ремонтне відділення автосервісу.
 * <p>
 * Конкретна реалізація {@link AbstractDepartment}.
 * Перевизначає {@code displayInfo()} з відповідним префіксом типу.
 * </p>
 */
class RepairDepartment extends AbstractDepartment {
    private static final long serialVersionUID = 1L;

    /**
     * Конструктор ремонтного відділення.
     *
     * @param name назва відділення
     */
    public RepairDepartment(String name) {
        super(name);
    }

    /**
     * {@inheritDoc}
     * <p>Виводить інформацію з позначкою «Ремонтне відділення».</p>
     */
    @Override
    public void displayInfo() {
        System.out.println("[Ремонтне відділення] " + getName());
    }

    /**
     * Повертає рядок з типом та назвою відділення для відображення у меню.
     *
     * @return форматований рядок
     */
    public String getDepartmentInfo() {
        return "[Ремонтне відділення] " + getName();
    }
}

/**
 * Діагностичний центр автосервісу.
 * <p>
 * Конкретна реалізація {@link AbstractDepartment}.
 * Перевизначає {@code displayInfo()} з відповідним префіксом типу.
 * </p>
 */
class DiagnosticDepartment extends AbstractDepartment {
    private static final long serialVersionUID = 1L;

    /**
     * Конструктор діагностичного центру.
     *
     * @param name назва відділення
     */
    public DiagnosticDepartment(String name) {
        super(name);
    }

    /**
     * {@inheritDoc}
     * <p>Виводить інформацію з позначкою «Діагностичний центр».</p>
     */
    @Override
    public void displayInfo() {
        System.out.println("[Діагностичний центр] " + getName());
    }

    /**
     * Повертає рядок з типом та назвою відділення для відображення у меню.
     *
     * @return форматований рядок
     */
    public String getDepartmentInfo() {
        return "[Діагностичний центр] " + getName();
    }
}

//  КЛАС АВТОСЕРВІСУ (Composition: AutoService → AbstractDepartment[])

/**
 * Головний клас автосервісу.
 * <p>
 * Використовує відношення <b>композиція</b>: містить список
 * об'єктів {@link AbstractDepartment}. Доступ до відділень
 * надається лише через безпечне API (без повернення прямого
 * посилання на внутрішню колекцію) — це усуває порушення
 * інкапсуляції попередньої версії.
 * </p>
 */
class AutoService extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    /** Внутрішній список відділень. Не повертається назовні напряму. */
    private final ArrayList<AbstractDepartment> departments;

    /**
     * Конструктор. Ініціалізує автосервіс і заповнює початкові дані.
     *
     * @param name назва автосервісу
     */
    public AutoService(String name) {
        super(name);
        this.departments = new ArrayList<>();
        initializeDefaultData();
    }

    // Безпечне API для доступу до відділень (замінює getDepartments())

    /**
     * Повертає кількість відділень автосервісу.
     *
     * @return кількість відділень
     */
    public int getDepartmentsCount() {
        return departments.size();
    }

    /**
     * Повертає відділення за індексом.
     *
     * @param index індекс відділення (0-based)
     * @return об'єкт відділення або {@code null}, якщо індекс за межами
     */
    public AbstractDepartment getDepartmentByIndex(int index) {
        if (index >= 0 && index < departments.size()) {
            return departments.get(index);
        }
        return null;
    }

    /**
     * Перевіряє, чи список відділень порожній.
     *
     * @return {@code true}, якщо відділень немає
     */
    public boolean isDepartmentsEmpty() {
        return departments.isEmpty();
    }

    /**
     * Додає нове відділення до автосервісу.
     *
     * @param dep об'єкт відділення
     */
    public void addDepartment(AbstractDepartment dep) {
        departments.add(dep);
    }

    /**
     * Видаляє відділення за індексом.
     *
     * @param index індекс відділення (0-based)
     */
    public void removeDepartment(int index) {
        if (index >= 0 && index < departments.size()) {
            departments.remove(index);
        }
    }

    /**
     * {@inheritDoc}
     * <p>Виводить назву автосервісу та список всіх відділень.</p>
     */
    @Override
    public void displayInfo() {
        System.out.println("=== Автосервіс: " + getName() + " ===");
        for (AbstractDepartment dep : departments) {
            dep.displayInfo(); // поліморфний виклик
        }
    }

    /**
     * Заповнює автосервіс початковими даними.
     * Демонструє поліморфізм: обидва об'єкти використовуються
     * через тип базового класу {@link AbstractDepartment}.
     */
    private void initializeDefaultData() {
        AbstractDepartment dep1 = new RepairDepartment("Шиномонтаж");
        dep1.addService("Балансування коліс", 200);
        dep1.addService("Заміна шини", 150);

        AbstractDepartment dep2 = new DiagnosticDepartment("Головна діагностика");
        dep2.addService("Комп'ютерна діагностика", 500);
        dep2.addService("Діагностика ходової", 400);

        departments.add(dep1);
        departments.add(dep2);
    }
}

//  КОШИК (CartItem + Cart)

/**
 * Позиція кошика замовлення.
 * <p>
 * Зберігає дані про одну послугу: назву, одиничну ціну та кількість.
 * Є immutable щодо назви та ціни; кількість змінюється через
 * безпечні методи {@code addQuantity} / {@code reduceQuantity}.
 * </p>
 */
class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Назва послуги. */
    private final String serviceName;

    /** Одинична ціна послуги у грн. */
    private final int unitPrice;

    /** Поточна кількість одиниць у замовленні. */
    private int quantity;

    /**
     * Конструктор позиції кошика з кількістю 1.
     *
     * @param serviceName назва послуги
     * @param unitPrice   ціна за одиницю (грн)
     */
    public CartItem(String serviceName, int unitPrice) {
        this.serviceName = serviceName;
        this.unitPrice   = unitPrice;
        this.quantity    = 1;
    }

    /** @return назва послуги */
    public String getServiceName() { return serviceName; }

    /** @return ціна за одиницю (грн) */
    public int getUnitPrice()      { return unitPrice; }

    /** @return поточна кількість */
    public int getQuantity()       { return quantity; }

    /**
     * Збільшує кількість на вказану величину.
     *
     * @param amount кількість для додавання (має бути &gt; 0)
     */
    public void addQuantity(int amount) {
        if (amount > 0) this.quantity += amount;
    }

    /**
     * Зменшує кількість на вказану величину.
     *
     * @param amount кількість для зменшення (має бути &gt; 0)
     */
    public void reduceQuantity(int amount) {
        if (amount > 0) this.quantity -= amount;
    }

    /**
     * Обчислює підсумкову вартість цієї позиції.
     *
     * @return {@code unitPrice * quantity}
     */
    public int getRowTotal() {
        return unitPrice * quantity;
    }
}

/**
 * Кошик клієнта.
 * <p>
 * Інкапсулює всю логіку роботи з замовленням: додавання,
 * видалення та підрахунок суми. Раніше ця логіка була
 * розпорошена по методах {@code InterfaceManager} — тепер
 * вона зосереджена тут (SRP).
 * </p>
 */
class Cart {
    /**
     * Дворівнева структура: назва відділення → (назва послуги → позиція).
     * Інкапсульована: зовнішній код не отримує пряме посилання.
     */
    private final LinkedHashMap<String, LinkedHashMap<String, CartItem>> items;

    /** Конструктор порожнього кошика. */
    public Cart() {
        this.items = new LinkedHashMap<>();
    }

    /**
     * Додає послугу до кошика. Якщо послуга вже є — збільшує кількість на 1.
     *
     * @param departmentName назва відділення
     * @param serviceName    назва послуги
     * @param price          ціна послуги
     */
    public void addItem(String departmentName, String serviceName, int price) {
        LinkedHashMap<String, CartItem> depCart =
            items.getOrDefault(departmentName, new LinkedHashMap<>());

        if (depCart.containsKey(serviceName)) {
            depCart.get(serviceName).addQuantity(1);
        } else {
            depCart.put(serviceName, new CartItem(serviceName, price));
        }
        items.put(departmentName, depCart);
    }

    /**
     * Видаляє позицію або зменшує її кількість.
     * Якщо {@code quantityToRemove} ≥ поточної кількості — позиція видаляється повністю.
     * Якщо після видалення відділення стає порожнім — запис відділення теж видаляється.
     *
     * @param departmentName назва відділення
     * @param serviceName    назва послуги
     * @param quantityToRemove кількість для видалення
     */
    public void removeItem(String departmentName, String serviceName, int quantityToRemove) {
        LinkedHashMap<String, CartItem> depCart = items.get(departmentName);
        if (depCart == null) return;

        CartItem item = depCart.get(serviceName);
        if (item == null) return;

        if (quantityToRemove >= item.getQuantity()) {
            depCart.remove(serviceName);
        } else {
            item.reduceQuantity(quantityToRemove);
        }

        if (depCart.isEmpty()) {
            items.remove(departmentName);
        }
    }

    /**
     * Підраховує загальну суму кошика.
     *
     * @return сума у грн
     */
    public int calculateTotal() {
        int total = 0;
        for (LinkedHashMap<String, CartItem> depCart : items.values()) {
            for (CartItem item : depCart.values()) {
                total += item.getRowTotal();
            }
        }
        return total;
    }

    /**
     * Підраховує загальну кількість одиниць послуг у кошику.
     *
     * @return кількість позицій
     */
    public int calculateItemsCount() {
        int count = 0;
        for (LinkedHashMap<String, CartItem> depCart : items.values()) {
            for (CartItem item : depCart.values()) {
                count += item.getQuantity();
            }
        }
        return count;
    }

    /**
     * Перевіряє, чи кошик порожній.
     *
     * @return {@code true}, якщо позицій немає
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Повертає незмінний перегляд вмісту кошика для читання.
     * Зовнішній код не може змінити структуру через це посилання.
     *
     * @return незмінне відображення: відділення → (послуга → позиція)
     */
    public Map<String, LinkedHashMap<String, CartItem>> getItemsView() {
        return Collections.unmodifiableMap(items);
    }
}

//  МЕНЕДЖЕР ІНТЕРФЕЙСУ

/**
 * Клас управління консольним інтерфейсом.
 * <p>
 * Відповідає <b>виключно</b> за:
 * <ul>
 *   <li>відображення меню та зчитування введення з консолі;</li>
 *   <li>виклик методів бізнес-логіки ({@link AutoService}, {@link Cart});</li>
 *   <li>серіалізацію/десеріалізацію даних автосервісу.</li>
 * </ul>
 * Перевірки наявності даних (порожній список, наявність послуги тощо)
 * перенесено до відповідних сутностей відповідно до SRP.
 * </p>
 */
class InterfaceManager {
    /** Об'єкт автосервісу, яким керує менеджер. */
    private AutoService auto;

    /** Сканер для зчитування введення. */
    private final Scanner scanner;

    /** Ім'я файлу для серіалізації стану автосервісу. */
    private static final String DATA_FILE = "autoservice_data.ser";

    /**
     * Конструктор. Ініціалізує сканер і завантажує збережені дані.
     */
    public InterfaceManager() {
        scanner = new Scanner(System.in, "UTF-8");
        loadData();
    }

    // Допоміжні методи UI

    /**
     * Очищує консоль (Windows: cls, інші: ANSI-послідовність).
     */
    private void clearConsole() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 30; i++) System.out.println();
        }
    }

    /**
     * Чекає на натискання Enter перед продовженням.
     */
    private void pause() {
        System.out.println("\n[Натисніть Enter для продовження...]");
        scanner.nextLine();
    }

    /**
     * Безпечно зчитує ціле число з консолі.
     * Повторює запит при некоректному введенні.
     *
     * @param prompt текст-підказка для користувача
     * @return введене ціле число
     */
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

    // Головне меню

    /**
     * Запускає головний цикл програми.
     * Перехід між режимами «Клієнт» і «Менеджер».
     */
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
                case 1: clientMenu();  break;
                case 2: managerMenu(); break;
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

    // РЕЖИМ МЕНЕДЖЕРА

    /**
     * Вхідна точка режиму менеджера.
     * Запитує ім'я, викликає {@link Manager#displayInfo()} (поліморфізм)
     * і відкриває панель управління.
     */
    private void managerMenu() {
        clearConsole();
        System.out.print("Введіть ваше ім'я (Менеджер): ");
        Manager currentManager = new Manager(scanner.nextLine());

        System.out.println();
        currentManager.displayInfo(); // поліморфний виклик
        pause();

        boolean inManager = true;
        while (inManager) {
            clearConsole();
            System.out.println("--- ПАНЕЛЬ МЕНЕДЖЕРА ---");
            System.out.println("Поточний користувач: " + currentManager.getName());
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
                case 2: addDepartmentFlow();    break;
                case 3: deleteDepartmentFlow(); break;
                case 4: manageServicesFlow();   break;
                case 5:
                    System.out.print("Введіть нову назву автосервісу: ");
                    auto.setName(scanner.nextLine());
                    System.out.println("Назву успішно змінено.");
                    pause();
                    break;
                case 0: inManager = false; break;
                default:
                    System.out.println("Некоректний вибір.");
                    pause();
            }
        }
    }

    /**
     * Виводить повну структуру автосервісу через безпечне API
     * без прямого доступу до внутрішньої колекції.
     */
    private void displayStructure() {
        System.out.println("--- СТРУКТУРА: " + auto.getName() + " ---");
        // Перевірка на порожність через метод сутності (SRP)
        if (auto.isDepartmentsEmpty()) {
            System.out.println("Відділення відсутні.");
            return;
        }
        for (int i = 0; i < auto.getDepartmentsCount(); i++) {
            AbstractDepartment dep = auto.getDepartmentByIndex(i);
            System.out.println("[" + (i + 1) + "] " + getDepartmentLabel(dep));
            for (String sName : dep.getServiceNames()) {
                System.out.println("    - " + sName + " (" + dep.getServicePrice(sName) + " грн)");
            }
        }
    }

    /**
     * Сценарій додавання нового відділення.
     * Менеджер обирає тип і вводить назву — автосервіс отримує новий об'єкт.
     */
    private void addDepartmentFlow() {
        clearConsole();
        System.out.println("Оберіть тип нового відділення:");
        System.out.println("1. Ремонтне відділення");
        System.out.println("2. Діагностичний центр");
        System.out.println("0. Відміна");

        int typeChoice = readInt("Ваш вибір: ");
        if (typeChoice == 0) return;

        if (typeChoice == 1 || typeChoice == 2) {
            System.out.print("Введіть назву нового відділення: ");
            String depName = scanner.nextLine();
            // Поліморфізм: вибір конкретного типу за введеним значенням
            auto.addDepartment(typeChoice == 1
                ? new RepairDepartment(depName)
                : new DiagnosticDepartment(depName));
            System.out.println("Відділення додано.");
        } else {
            System.out.println("Некоректний вибір типу.");
        }
        pause();
    }

    /**
     * Сценарій видалення відділення.
     * Перевірка індексу відбувається у {@link AutoService#removeDepartment}.
     */
    private void deleteDepartmentFlow() {
        clearConsole();
        displayStructure();
        int index = readInt("\nВведіть номер відділення для видалення (0 для відміни): ") - 1;
        if (index >= 0 && index < auto.getDepartmentsCount()) {
            auto.removeDepartment(index);
            System.out.println("Відділення видалено.");
        }
        pause();
    }

    /**
     * Сценарій управління послугами обраного відділення.
     * Логіка перевірок (наявність послуги, дублікат) делегована
     * до {@link AbstractDepartment} (SRP).
     */
    private void manageServicesFlow() {
        clearConsole();
        displayStructure();
        int depIndex = readInt("\nОберіть відділення для керування послугами (0 для відміни): ") - 1;
        if (depIndex < 0 || depIndex >= auto.getDepartmentsCount()) return;

        AbstractDepartment dep = auto.getDepartmentByIndex(depIndex);

        boolean managing = true;
        while (managing) {
            clearConsole();
            System.out.println("--- Послуги: " + getDepartmentLabel(dep) + " ---");
            List<String> sKeys = dep.getServiceNames();

            if (dep.isServicesEmpty()) {
                System.out.println("Послуги відсутні.");
            } else {
                for (int i = 0; i < sKeys.size(); i++) {
                    System.out.println("[" + (i + 1) + "] " + sKeys.get(i)
                        + " - " + dep.getServicePrice(sKeys.get(i)) + " грн");
                }
            }

            System.out.println("\n1. Додати послугу");
            System.out.println("2. Видалити послугу");
            System.out.println("3. Редагувати послугу");
            System.out.println("0. Назад");

            int choice = readInt("Ваш вибір: ");
            switch (choice) {
                case 1: addServiceFlow(dep);        break;
                case 2: deleteServiceFlow(dep);     break;
                case 3: editServiceFlow(dep);       break;
                case 0: managing = false;           break;
                default:
                    System.out.println("Некоректний вибір.");
                    pause();
            }
        }
    }

    /**
     * Сценарій додавання послуги до прайс-листа відділення.
     * Перевірка на дублікат делегована до {@link AbstractDepartment#containsService}.
     *
     * @param dep відділення, до якого додається послуга
     */
    private void addServiceFlow(AbstractDepartment dep) {
        boolean adding = true;
        while (adding) {
            System.out.print("Назва послуги: ");
            String sName = scanner.nextLine();

            // Перевірка на дублікат — через метод сутності (SRP)
            if (dep.containsService(sName)) {
                System.out.println("\nУВАГА! Послуга з такою назвою вже існує.");
                System.out.println("1. Замінити існуючу послугу (оновити ціну)");
                System.out.println("2. Ввести іншу назву");
                System.out.println("0. Скасувати");
                int conflictChoice = readInt("Ваш вибір: ");

                if (conflictChoice == 1) {
                    dep.addService(sName, readInt("Нова ціна (грн): "));
                    System.out.println("Послугу оновлено.");
                    adding = false;
                } else if (conflictChoice == 2) {
                    // продовжити цикл
                } else {
                    System.out.println("Операцію скасовано.");
                    adding = false;
                }
            } else {
                dep.addService(sName, readInt("Ціна (грн): "));
                System.out.println("Послугу успішно додано.");
                adding = false;
            }
        }
        pause();
    }

    /**
     * Сценарій видалення послуги з прайс-листа відділення.
     * Перевірка на порожність через {@link AbstractDepartment#isServicesEmpty()}.
     *
     * @param dep відділення
     */
    private void deleteServiceFlow(AbstractDepartment dep) {
        // Перевірка через метод сутності (SRP)
        if (dep.isServicesEmpty()) {
            System.out.println("Немає послуг для видалення.");
            pause();
            return;
        }
        List<String> sKeys = dep.getServiceNames();
        int delIndex = readInt("Введіть номер послуги для видалення: ") - 1;
        if (delIndex >= 0 && delIndex < sKeys.size()) {
            dep.removeService(sKeys.get(delIndex));
            System.out.println("Послугу видалено.");
        } else {
            System.out.println("Некоректний номер.");
        }
        pause();
    }

    /**
     * Сценарій редагування послуги: зміна назви та/або ціни.
     * Перевірка на дублікат нової назви делегована до
     * {@link AbstractDepartment#containsService}.
     *
     * @param dep відділення
     */
    private void editServiceFlow(AbstractDepartment dep) {
        if (dep.isServicesEmpty()) {
            System.out.println("Немає послуг для редагування.");
            pause();
            return;
        }
        List<String> sKeys = dep.getServiceNames();
        int editIndex = readInt("Введіть номер послуги для редагування: ") - 1;

        if (editIndex >= 0 && editIndex < sKeys.size()) {
            String oldName = sKeys.get(editIndex);
            System.out.print("Нова назва (Enter — залишити '" + oldName + "'): ");
            String newName = scanner.nextLine().trim();
            if (newName.isEmpty()) newName = oldName;

            if (!newName.equals(oldName) && dep.containsService(newName)) {
                System.out.println("\nПомилка: послуга з назвою '" + newName + "' вже існує.");
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
    }

    // РЕЖИМ КЛІЄНТА

    /**
     * Вхідна точка режиму клієнта.
     * Запитує ім'я, викликає {@link Client#displayInfo()} (поліморфізм)
     * і відкриває меню кошика.
     */
    private void clientMenu() {
        clearConsole();
        System.out.print("Введіть ваше ім'я: ");
        Client currentClient = new Client(scanner.nextLine());

        System.out.println();
        currentClient.displayInfo(); // поліморфний виклик
        pause();

        // Перевірка наявності відділень через безпечне API AutoService (SRP)
        if (auto.isDepartmentsEmpty()) {
            System.out.println("Вибачте, наразі немає доступних відділень.");
            pause();
            return;
        }

        Cart cart = currentClient.getCart();
        boolean ordering = true;

        while (ordering) {
            clearConsole();
            // Підрахунок делегований до Cart (SRP)
            printCartHeader(currentClient.getName(), cart);

            System.out.println("\nДії з кошиком:");
            System.out.println("1. Додати послугу (Перейти до списку відділень)");
            System.out.println("2. Видалити послугу з кошика");
            System.out.println("0. Завершити замовлення та сформувати чек");

            int action = readInt("Ваш вибір: ");
            switch (action) {
                case 1: addServiceToCartFlow(cart);      break;
                case 2: removeServiceFromCartFlow(cart); break;
                case 0: ordering = false;                break;
                default:
                    System.out.println("Некоректний вибір.");
                    pause();
            }
        }

        if (!cart.isEmpty()) {
            clearConsole();
            generateReceipt(currentClient.getName(), cart);
        } else {
            System.out.println("\nВи нічого не замовили. Скасування.");
        }
        pause();
    }

    /**
     * Виводить заголовок кошика з підсумками.
     * Використовує {@link Cart#calculateTotal()} і {@link Cart#calculateItemsCount()}.
     *
     * @param clientName ім'я клієнта
     * @param cart       кошик клієнта
     */
    private void printCartHeader(String clientName, Cart cart) {
        System.out.println("=== КОШИК КЛІЄНТА " + clientName
            + " (" + cart.calculateItemsCount() + " позицій на суму "
            + cart.calculateTotal() + " грн) ===");

        if (cart.isEmpty()) {
            System.out.println("  [Кошик порожній]");
        } else {
            for (Map.Entry<String, LinkedHashMap<String, CartItem>> entry
                    : cart.getItemsView().entrySet()) {
                System.out.println("Відділення [" + entry.getKey() + "]:");
                for (CartItem item : entry.getValue().values()) {
                    if (item.getQuantity() > 1) {
                        System.out.printf("  - %s x%d ........... %d грн (по %d грн)%n",
                            item.getServiceName(), item.getQuantity(),
                            item.getRowTotal(), item.getUnitPrice());
                    } else {
                        System.out.printf("  - %s ........... %d грн%n",
                            item.getServiceName(), item.getRowTotal());
                    }
                }
            }
        }
        System.out.println("=========================================================");
    }

    /**
     * Сценарій додавання послуги до кошика.
     * Клієнт обирає відділення, потім послугу.
     * Вся логіка запису — в {@link Cart#addItem}.
     *
     * @param cart кошик клієнта
     */
    private void addServiceToCartFlow(Cart cart) {
        clearConsole();
        System.out.println("Оберіть відділення:");
        for (int i = 0; i < auto.getDepartmentsCount(); i++) {
            System.out.println("[" + (i + 1) + "] "
                + auto.getDepartmentByIndex(i).getName());
        }
        System.out.println("[0] Назад");

        int depChoice = readInt("Ваш вибір: ") - 1;
        if (depChoice == -1) return;
        if (depChoice < 0 || depChoice >= auto.getDepartmentsCount()) {
            System.out.println("Некоректний вибір.");
            pause();
            return;
        }

        AbstractDepartment chosenDep = auto.getDepartmentByIndex(depChoice);

        // Перевірка через метод сутності (SRP)
        if (chosenDep.isServicesEmpty()) {
            System.out.println("У цьому відділенні наразі немає послуг.");
            pause();
            return;
        }

        clearConsole();
        System.out.println("--- Послуги: " + getDepartmentLabel(chosenDep) + " ---");
        List<String> serviceKeys = chosenDep.getServiceNames();
        for (int i = 0; i < serviceKeys.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + serviceKeys.get(i)
                + " - " + chosenDep.getServicePrice(serviceKeys.get(i)) + " грн");
        }
        System.out.println("\n[0] Назад");

        int sChoice = readInt("Ваш вибір: ") - 1;
        if (sChoice == -1) return;

        if (sChoice >= 0 && sChoice < serviceKeys.size()) {
            String selectedService = serviceKeys.get(sChoice);
            int price = chosenDep.getServicePrice(selectedService);
            // Вся логіка додавання — в Cart (SRP)
            cart.addItem(chosenDep.getName(), selectedService, price);
            System.out.println("\nПослугу '" + selectedService + "' успішно додано до замовлення!");
            pause();
        } else {
            System.out.println("Некоректний вибір послуги.");
            pause();
        }
    }

    /**
     * Сценарій видалення позиції з кошика.
     * Вся логіка видалення — в {@link Cart#removeItem}.
     *
     * @param cart кошик клієнта
     */
    private void removeServiceFromCartFlow(Cart cart) {
        if (cart.isEmpty()) {
            System.out.println("Кошик порожній, видаляти нічого.");
            pause();
            return;
        }

        clearConsole();
        System.out.println("=== ВИДАЛЕННЯ З КОШИКА ===");

        List<String> flatDepNames = new ArrayList<>();
        List<CartItem> flatItems  = new ArrayList<>();
        int idx = 1;

        for (Map.Entry<String, LinkedHashMap<String, CartItem>> depEntry
                : cart.getItemsView().entrySet()) {
            System.out.println("Відділення [" + depEntry.getKey() + "]:");
            for (CartItem item : depEntry.getValue().values()) {
                flatDepNames.add(depEntry.getKey());
                flatItems.add(item);
                System.out.println("  [" + idx + "] " + item.getServiceName()
                    + " x" + item.getQuantity());
                idx++;
            }
        }

        System.out.println("\n[0] Відміна");
        int delChoice = readInt("Оберіть номер позиції для видалення: ") - 1;
        if (delChoice == -1) return;

        if (delChoice >= 0 && delChoice < flatItems.size()) {
            CartItem itemToDel = flatItems.get(delChoice);
            String depName = flatDepNames.get(delChoice);
            int qToDel;

            if (itemToDel.getQuantity() > 1) {
                qToDel = readInt("У вас " + itemToDel.getQuantity()
                    + " шт. цієї послуги. Скільки штук видалити? ");
            } else {
                qToDel = 1;
            }

            if (qToDel > 0) {
                // Вся логіка видалення — в Cart (SRP)
                cart.removeItem(depName, itemToDel.getServiceName(), qToDel);
                System.out.println("Виконано.");
            } else {
                System.out.println("Скасовано.");
            }
        } else {
            System.out.println("Некоректний вибір.");
        }
        pause();
    }

    /**
     * Формує та виводить чек замовлення клієнта.
     * Також зберігає чек у текстовий файл.
     *
     * @param clientName ім'я клієнта
     * @param cart       кошик з замовленням
     */
    private void generateReceipt(String clientName, Cart cart) {
        int total = cart.calculateTotal();

        StringBuilder receipt = new StringBuilder();
        receipt.append("=========================================\n");
        receipt.append("          ЧЕК АВТОСЕРВІСУ                \n");
        receipt.append("=========================================\n");
        receipt.append("Клієнт: ").append(clientName).append("\n\n");
        receipt.append("Деталізація замовлення:\n");

        for (Map.Entry<String, LinkedHashMap<String, CartItem>> entry
                : cart.getItemsView().entrySet()) {
            receipt.append("Відділення [").append(entry.getKey()).append("]:\n");
            for (CartItem item : entry.getValue().values()) {
                if (item.getQuantity() > 1) {
                    receipt.append(String.format("  - %-25s x%-2d ...... %4d грн (по %d грн)%n",
                        item.getServiceName(), item.getQuantity(),
                        item.getRowTotal(), item.getUnitPrice()));
                } else {
                    receipt.append(String.format("  - %-25s    ...... %4d грн%n",
                        item.getServiceName(), item.getRowTotal()));
                }
            }
        }

        receipt.append("-----------------------------------------\n");
        receipt.append("ДО СПЛАТИ: ").append(total).append(" грн\n");
        receipt.append("=========================================\n");

        System.out.print(receipt.toString());

        String fileName = "receipt_" + clientName + ".txt";
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(receipt + "\n");
            System.out.println("\n(Чек успішно збережено у файл " + fileName + ")");
        } catch (IOException e) {
            System.out.println("\nПомилка при збереженні чека: " + e.getMessage());
        }
    }

    // Серіалізація

    /**
     * Зберігає поточний стан {@link AutoService} у файл через серіалізацію.
     */
    private void saveData() {
        try (ObjectOutputStream oos =
                new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(auto);
            System.out.println("Дані автосервісу успішно збережено у базу.");
        } catch (IOException e) {
            System.out.println("Помилка при збереженні даних: " + e.getMessage());
        }
    }

    /**
     * Завантажує стан {@link AutoService} з файлу або створює новий об'єкт.
     */
    private void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois =
                    new ObjectInputStream(new FileInputStream(file))) {
                auto = (AutoService) ois.readObject();
                return;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Помилка завантаження даних. Створено нову базу.");
            }
        }
        auto = new AutoService("АвтоПлюс");
    }

    // Утиліти

    /**
     * Повертає текстову мітку відділення (тип + назва).
     * Метод централізує отримання рядка: якщо об'єкт є ремонтним —
     * повертає один рядок, якщо діагностичним — інший.
     *
     * @param dep відділення
     * @return текстова мітка
     */
    private String getDepartmentLabel(AbstractDepartment dep) {
        if (dep instanceof RepairDepartment) {
            return ((RepairDepartment) dep).getDepartmentInfo();
        } else if (dep instanceof DiagnosticDepartment) {
            return ((DiagnosticDepartment) dep).getDepartmentInfo();
        }
        return dep.getName();
    }
}

//  ТОЧКА ВХОДУ

/**
 * Клас-запускач програми.
 * Містить точку входу {@code main}: створює {@link InterfaceManager}
 * і передає керування йому
 */
public class Kurs_Auto {

    /**
     * Точка входу JVM.
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void main(String[] args) {
        InterfaceManager app = new InterfaceManager();
        app.start();
    }
}
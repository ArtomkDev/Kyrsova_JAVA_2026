# Архітектура програмного забезпечення "Автосервіс"

Даний документ описує об'єктно-орієнтовану архітектуру консольного додатку "Автосервіс", створеного в рамках курсової роботи з ООП на мові Java.

## UML Діаграма Класів
```mermaid
classDiagram
    class ServiceDepartment {
        -String name_dep
        -LinkedHashMap~String, Integer~ price
        +ServiceDepartment()
        +ServiceDepartment(String name)
        #getName() String
        #setNameDep(String name) void
        #setPrice(String[] service, int[] cost) void
        #getPrice() LinkedHashMap~String, Integer~
    }

    class AutoService {
        -String name
        -int num_of_dep
        #ArrayList~ServiceDepartment~ departments
        -String client_name
        -int choicedep
        -int choiceservice
        +AutoService()
        +AutoService(String n, int num)
        #getChoiceDep() int
        #getStrChoiceService() String
        #getPrChoiceService() int
        #getChoiceService() int
        #setChoiceDep() void
        #setServiceChoice() void
        #setNameClient(String name) void
        #getNameClient() String
        #getNumofDep() int
        #getNameAuto() String
        #setService_Departments() void
        -newDep() ServiceDepartment
    }

    class Test {
        #ArrayList~String~ info_manager$
        #ArrayList~String~ info_client$
        #AutoService auto$
        #testing_manager() void$
        #InputDataManager() void$
        #Demonst(ArrayList~String~ info) void$
        #testing_client() void$
        #DataInputClient() void$
        #role() int$
    }

    class Kurs_Auto {
        +main(String[] args) void$
    }

    ServiceDepartment <|-- AutoService : Inheritance (Extends)
    Test o-- AutoService : Aggregation
    Kurs_Auto --> Test : Calls
import java.time.LocalDate;

/// TODO:
///
/// 1. How to correctly handle the case of NON-BAG association with an attribute? What should be the multiplicities
/// 2. Are my portfolio qualified association multiplicities correct?
/// 3. How to model things like composition, if there is no extent? Cause then there is no such thing as 'deletion' therefore
///    behaviour on deletion is unhandleble.
/// 4. Is my `getExtent` method correct approach for allowing the extent access only for the child classes own extents?
/// 5. Should the remove() be private and is it a correct approach?
/// 6. Can my inner class for composition be public (Report of Property)?
/// 7. Check what creation patterns are correct i.e. which class should create which?
/// 8. Should remove be public?
/// 9. IMPORTANT: For single reverse bindings remove final field and model that it will be set to null on dropping.

public class Main {
    public static void main (String[] argv) {

        System.out.println("Basic association creation 1 to 0..*");

        System.out.println("Approach 1:");
        Employee emp1 = new Employee("Jacob", "Rockfield");
        Portfolio port1 = new Portfolio("Cheap Offers", "Properties focusing on price to quality balance.", emp1);
        System.out.println("Reverse connection check: " + (emp1 == port1.getEmployee() && port1 == emp1.getPortfolios().getFirst()));
        System.out.println(emp1);
        System.out.println(port1);

        System.out.println("Approach 2:");
        Employee emp2 = new Employee("Chuck", "Norris");
        Portfolio port2 = emp2.createPortfolio("Chuck's Portfolio", "The best possible properties.");
        System.out.println("Reverse connection check: " + (emp2 == port2.getEmployee() && port2 == emp2.getPortfolios().getFirst()));
        System.out.println(emp2);
        System.out.println(port2);


        System.out.println();
        System.out.println("=========================================================================================");
        System.out.println();


        System.out.println("Qualified association creation 0..1 to 0..*");
        Property prop1 = new Property("Dutch House", 155.0, "st. Harris 10A");
        Property prop2 = new Property("Warsaw Tower", 155.0, "ul. Kowalskiego 1B");

        System.out.println("Approach 1:");
        port1.addProperty("Cheap Dutch House", prop1);
        System.out.println("Reverse connection check: " + (
                port1.getProperties().get("Cheap Dutch House") == prop1
                && prop1.getPortfolios().getFirst() == port1
        ));
        System.out.println(port1);

        System.out.println("Approach 2:");
        prop2.addToPortfolio("Warsaw Highest Building", port2);
        System.out.println("Reverse connection check: " + (
                port2.getProperties().get("Warsaw Highest Building") == prop2
                        && prop2.getPortfolios().getFirst() == port2
        ));
        System.out.println(port2);


        System.out.println();
        System.out.println("=========================================================================================");
        System.out.println();


        System.out.println("Composition association creation 0..* to 1");
        prop1.addReport("Cash flow based valuation report", 1_560_055.70);
        prop1.addReport("Comparison based valuation report", 1_578_000.00);
        System.out.println("Reverse connection check: " +
                prop1.getReports().stream().allMatch(report -> report.getProperty() == prop1)
        );
        System.out.println(prop1);


        System.out.println();
        System.out.println("=========================================================================================");
        System.out.println();


        System.out.println("Association with an attribute creation 0..* to 1");
        Owner owner1 = new Owner("Travis", "Scott");
        Owner owner2 = new Owner("Some", "Owner");
        Contract contract1 = new Contract(LocalDate.now(), LocalDate.now().plusYears(1), 1_400_000.00, prop1, owner1);
        try {
            new Contract(LocalDate.now(), LocalDate.now().plusYears(1), 1_400_000.00, prop1, owner2);
        } catch (IllegalArgumentException ex) {
            System.out.println("Correctly thrown exception for overlapping dates on the property contracts side.");
        }

        System.out.println("Reverse connection check: " +
                ((contract1.getOwner() == owner1 && owner1.getContracts().getFirst() == contract1) &&
                (contract1.getProperty() == prop1 && prop1.getContracts().getFirst() == contract1))
        );
        System.out.println("Testing bag association type...");
        Contract contract2 = new Contract(LocalDate.now().plusYears(1), LocalDate.now().plusYears(2), 1_300_000.00, prop1, owner2);
        System.out.println("Success.");
        System.out.println(contract1);
        System.out.println(contract2);



        System.out.println();
        System.out.println("=========================================================================================");
        System.out.println();

        System.out.println("[TEST] Dropping OWNER");
        System.out.println("Finding CONTRACT from PROPERTY");
        System.out.println("Found: " + prop1.getContracts().stream().anyMatch(c -> c.getOwner() == owner2));
        System.out.println("Dropping OWNER, which should also affect the PROPERTY");
        owner2.drop();
        System.out.println("Finding CONTRACT from PROPERTY again");
        System.out.println("Found: " + prop1.getContracts().stream().anyMatch(c -> c.getOwner() == owner2));
        System.out.println();

        System.out.println("[TEST] Dropping PROPERTY");
        System.out.println("Finding PROPERTY from PORTFOLIO");
        System.out.println("Found: " + port1.getProperties().values().stream().anyMatch(p -> p == prop1));
        System.out.println("Finding PROPERTY from OWNER");
        System.out.println("Found: " + owner1.getContracts().stream().anyMatch(c -> c.getProperty() == prop1));

        System.out.println("Dropping PROPERTY, which should also affect the PORTFOLIO and OWNER (REPORT is internally handled)");
        prop1.drop();
        System.out.println("Finding PROPERTY from PORTFOLIO");
        System.out.println("Found: " + port1.getProperties().values().stream().anyMatch(p -> p == prop1));
        System.out.println("Finding PROPERTY from OWNER");
        System.out.println("Found: " + owner1.getContracts().stream().anyMatch(c -> c.getProperty() == prop1));
        System.out.println();

        System.out.println("[TEST] Dropping PORTFOLIO");
        System.out.println("Trying to find PORTFOLIO from EMPLOYEE");
        System.out.println("Found: " + emp2.getPortfolios().stream().anyMatch(p -> p == port2));
        System.out.println("Trying to find PORTFOLIO from PROPERTY");
        System.out.println("Found: " + prop2.getPortfolios().stream().anyMatch(p -> p == port2));
        System.out.println("Dropping, which should affect PROPERTY and EMPLOYEE");
        port2.drop();
        System.out.println("Trying to find PORTFOLIO from EMPLOYEE");
        System.out.println("Found: " + emp2.getPortfolios().stream().anyMatch(p -> p == port2));
        System.out.println("Trying to find PORTFOLIO from PROPERTY");
        System.out.println("Found: " + prop2.getPortfolios().stream().anyMatch(p -> p == port2));
    }
}

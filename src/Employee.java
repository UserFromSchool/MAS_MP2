import java.util.HashSet;
import java.util.List;

public class Employee {

    private final String name;
    private final String surname;
    private final HashSet<Portfolio> portfolios;

    public Employee(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.portfolios = new HashSet<>();
    }

    public void addPortfolio(Portfolio portfolio) {
        this.portfolios.add(portfolio);
    }

    public Portfolio createPortfolio(String name, String description) {
        return new Portfolio(name, description, this);
    }

    public void dropPortfolio(Portfolio portfolio) {
        portfolios.remove(portfolio);
        if (!portfolio.getProperties().isEmpty()) {
            portfolio.drop();
        }
    }

    public List<Portfolio> getPortfolios() {
        return portfolios.stream().toList();
    }

    @Override
    public String toString() {
        return "-- EMPLOYEE" + '\n' +
                "-- Name                 " + name + '\n' +
                "-- Surname              " + surname + '\n';
    }
}

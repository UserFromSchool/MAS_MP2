import java.util.ArrayList;
import java.util.List;

public class Employee {

    private final String name;
    private final String surname;
    private final ArrayList<Portfolio> portfolios;

    public Employee(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.portfolios = new ArrayList<>();
    }

    public void addPortfolio(Portfolio portfolio)
    {
        if (portfolios.contains(portfolio)) {
            throw new IllegalArgumentException("Can't add a portfolio, which already exists.");
        }
        this.portfolios.add(portfolio);
    }

    public Portfolio createPortfolio(String name, String description) {
        return new Portfolio(name, description, this);
    }

    public void dropPortfolio(Portfolio portfolio) {
        if (!portfolios.contains(portfolio)) {
            throw new IllegalArgumentException("This employee did not create this portfolio.");
        }
        portfolios.remove(portfolio);
        if (portfolio.getEmployee() == this) {
            portfolio.drop();
        }
    }

    public void drop() {
        portfolios.stream().toList().forEach(Portfolio::drop);
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

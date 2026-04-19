import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Property {

    private final String name;
    private final double area;
    private final String address;
    private final ArrayList<Report> reports;
    private final ArrayList<Portfolio> portfolios;
    private final ArrayList<Contract> contracts;

    public class Report {

        private final String description;
        private final double evaluatedPrice;
        private final LocalDate createdAt;

        private Report(String description, double evaluatedPrice, LocalDate createdAt) {
            this.description = description;
            this.evaluatedPrice = evaluatedPrice;
            this.createdAt = createdAt;
            Property.this.reports.add(this);
        }

        public Property getProperty() {
            return Property.this;
        }

        @Override
        public String toString() {
            return "-- REPORT" + '\n' +
                    "-- Description          " + description + '\n' +
                    "-- Evaluated Price      " + evaluatedPrice + '\n' +
                    "-- Created At           " + createdAt + '\n';
        }
    }

    public Property(String name, double area, String address) {
        this.name = name;
        this.area = area;
        this.address = address;
        this.reports = new ArrayList<>();
        this.portfolios = new ArrayList<>();
        this.contracts = new ArrayList<>();
    }

    public void addToPortfolio(String nameInPortfolio, Portfolio portfolio) {
        if (this.portfolios.contains(portfolio)) {
            throw new IllegalArgumentException("This portfolio already contains this property property.");
        }
        this.portfolios.add(portfolio);
        if (!portfolio.getProperties().containsValue(this)) {
            portfolio.addProperty(nameInPortfolio, this);
        }
    }

    public Report addReport(String description, double evaluatedPrice) {
        return new Report(description, evaluatedPrice, LocalDate.now());
    }

    public void addContract(Contract contract) {
        if (contracts.contains(contract)) {
            throw new IllegalArgumentException("Can't add the same contract to the history or property contracts.");
        }
        if (contract.getProperty() != this) {
            throw new IllegalArgumentException("This contract can't be signed for this property as it was already signed for other.");
        }
        contracts.add(contract);
    }

    public List<Property.Report> getReports() {
        return reports.stream().toList();
    }

    public List<Portfolio> getPortfolios() {
        return portfolios.stream().toList();
    }

    public List<Contract> getContracts() {
        return contracts.stream().toList();
    }

    public void dropContract(Contract contract) {
        if (!contracts.contains(contract)) {
            throw new IllegalArgumentException("This contract was not signed for this property. Therefore, it can't be deleted from property.");
        }
        contracts.removeIf(c -> c == contract);
        contract.drop();
    }

    public void dropPortfolio(Portfolio portfolio) {
        if (!portfolios.contains(portfolio)) {
            throw new IllegalArgumentException("This property was not registered in this portfolio. Therefore, it can't be deleted from property.");
        }
        portfolios.remove(portfolio);
        if (portfolio.getProperties().containsValue(this)) {
            portfolio.dropProperty(this);
        }
    }

    public void dropReport(Report report) {
        if (!reports.contains(report)) {
            throw new IllegalArgumentException("Couldn't find such a report for this property.");
        }
        this.reports.removeIf(r -> r == report);
    }

    public void drop() {
        contracts.stream().toList().forEach(Contract::drop);
        portfolios.stream().toList().forEach(p -> p.dropProperty(this));
        reports.clear();
    }

    @Override
    public String toString() {
        return "-- PROPERTY" + '\n' +
                "-- Name                 " + name + '\n' +
                "-- Area                 " + area + '\n' +
                "-- Address              " + address + '\n' +
                "-- Reports              " + "\n    " + getReports()
                .stream().map(Report::toString)
                .map(s -> String.join("\n    ", s.split("\n")))
                .collect(Collectors.joining("\n    "));
    }
}

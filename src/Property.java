import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Property {

    private final String name;
    private final double area;
    private final String address;
    private final HashSet<Report> reports;
    private final HashSet<Portfolio> portfolios;
    private final HashSet<Contract> contracts;

    public class Report {

        private final String description;
        private final double evaluatedPrice;
        private final LocalDate createdAt;

        private Report(String description, double evaluatedPrice, LocalDate createdAt) {
            super();
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
        super();
        this.name = name;
        this.area = area;
        this.address = address;
        this.reports = new HashSet<>();
        this.portfolios = new HashSet<>();
        this.contracts = new HashSet<>();
    }

    public void addToPortfolio(String nameInPortfolio, Portfolio portfolio) {
        this.portfolios.add(portfolio);
        if (!portfolio.getProperties().containsValue(this)) {
            portfolio.addProperty(nameInPortfolio, this);
        }
    }

    public void addReport(String description, double evaluatedPrice) {
        this.reports.add(new Report(description, evaluatedPrice, LocalDate.now()));
    }

    public void addContract(Contract contract) {
        if (contracts.contains(contract)) {
            throw new IllegalArgumentException("Can't add the same contract to the history or property contracts.");
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
        contracts.removeIf(c -> c == contract);
        contract.drop();
    }

    public void dropPortfolio(Portfolio portfolio) {
        portfolios.remove(portfolio);
        if (portfolio.getProperties().containsValue(this)) {
            portfolio.dropProperty(this);
        }
    }

    public void drop() {
        contracts.forEach(Contract::drop);
        portfolios.forEach(p -> p.dropProperty(this));
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

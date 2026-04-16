import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

public class Portfolio {

    private final String name;
    private final String description;
    private final HashMap<String, Property> properties;
    private Employee employee;

    public Portfolio(String name, String description, Employee employee) {
        super();
        this.name = name;
        this.description = description;
        this.properties = new HashMap<>();
        this.employee = employee;
        employee.addPortfolio(this);
    }

    public void addProperty(String name, Property property) {
        this.properties.put(name, property);
        if (property.getPortfolios().stream().noneMatch(p -> p == this)) {
            property.addToPortfolio(name, this);
        }
    }

    public void dropProperty(Property property) {
        Optional<String> key = properties.keySet().stream().filter(k -> properties.get(k) == property).findFirst();
        if (key.isEmpty()) {
            throw new IllegalArgumentException("The portfolio does not contain property under the name of " + key);
        }
        properties.remove(key.get());
        if (property.getPortfolios().contains(this)) {
            property.dropPortfolio(this);
        }
    }

    public void drop() {
        properties.values().stream().toList().forEach(p -> p.dropPortfolio(this));
        if (employee.getPortfolios().contains(this)) {
            employee.dropPortfolio(this);
        }
        employee = null;
    }

    public int getSize() {
        return properties.size();
    }

    public HashMap<String, Property> getProperties() {
        return new HashMap<>(properties);
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public String toString() {
        return "-- PORTFOLIO" + '\n' +
                "-- Name                 " + name + '\n' +
                "-- Description          " + description + '\n' +
                "-- Size                 " + getSize() + " properties\n" +
                "-- Properties           " + "\n    " + getProperties().entrySet()
                .stream().map((entry) -> "-- NAMED: " + entry.getKey() + "\n" + entry.getValue())
                .map(s -> String.join("\n    ", s.split("\n")))
                .collect(Collectors.joining("\n    "));
    }
}

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

public class Portfolio {

    private final String name;
    private final String description;
    private final HashMap<String, Property> properties;
    private Employee employee;

    public Portfolio(String name, String description, Employee employee) {
        this.name = name;
        this.description = description;
        this.properties = new HashMap<>();
        this.employee = employee;
        employee.addPortfolio(this);
    }

    public void setEmployee(Employee employee) {
        if (employee == this.employee) return;
        Employee emp = this.employee;
        this.employee = employee;
        emp.dropPortfolio(this);
        this.employee.addPortfolio(this);
    }

    public void addProperty(String name, Property property) {
        if (properties.containsKey(name) || properties.containsValue(property)) {
            throw new IllegalArgumentException("Such property or name was already registered in this portfolio.");
        }
        this.properties.put(name, property);
        if (property.getPortfolios().stream().noneMatch(p -> p == this)) {
            property.addToPortfolio(name, this);
        }
    }

    public void dropProperty(Property property) {
        Optional<String> key = properties.keySet().stream().filter(k -> properties.get(k) == property).findFirst();
        if (key.isEmpty()) {
            throw new IllegalArgumentException("The portfolio does not contain such property. Therefore, it can't be deleted.");
        }
        properties.remove(key.get());
        if (property.getPortfolios().contains(this)) {
            property.dropPortfolio(this);
        }
    }

    public void drop() {
        properties.values().stream().toList().forEach(p -> p.dropPortfolio(this));
        Employee emp = employee;
        employee = null;
        if (emp.getPortfolios().contains(this)) {
            emp.dropPortfolio(this);
        }
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Owner extends ObjectPlus {

    private final String name;
    private final String surname;
    private final HashSet<Contract> contracts;

    public Owner(String name, String surname) {
        super();
        this.name = name;
        this.surname = surname;
        this.contracts = new HashSet<>();
    }

    public void addContract(Contract contract) {
        if (contracts.contains(contract)) {
            throw new IllegalArgumentException("Owner already has signed this contract.");
        }
        contracts.add(contract);
    }

    public List<Contract> getContracts() {
        return contracts.stream().toList();
    }

    public void dropProperty(Property property) {
        contracts.removeIf(c -> c.getProperty() == property);
        if (property.getContracts().stream().anyMatch(c -> c.getOwner() == this)) {
            property.dropOwner(this);
        }
    }

    public void drop() {
        Set<Property> properties = contracts.stream().map(Contract::getProperty).collect(Collectors.toSet());
        properties.forEach(p -> p.dropOwner(this));
        contracts.clear();
    }

    @Override
    public String toString() {
        return "-- OWNER" + '\n' +
                "-- Name                 " + name + '\n' +
                "-- Surname              " + surname + '\n';
    }
}

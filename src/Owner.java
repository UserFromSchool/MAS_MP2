import java.util.ArrayList;
import java.util.List;

public class Owner {

    private final String name;
    private final String surname;
    private final ArrayList<Contract> contracts;

    public Owner(String name, String surname) {
        super();
        this.name = name;
        this.surname = surname;
        this.contracts = new ArrayList<>();
    }
    public void addContract(Contract contract) {
        if (contracts.contains(contract)) {
            throw new IllegalArgumentException("Owner already has signed this contract.");
        }
        if (contract.getOwner() != this) {
            throw new IllegalArgumentException("This contract can't be signed by this owner as it was already signed by other.");
        }
        contracts.add(contract);
    }

    public List<Contract> getContracts() {
        return contracts.stream().toList();
    }

    public void dropContract(Contract contract) {
        if (!contracts.contains(contract)) {
            throw new IllegalArgumentException("This contract was not signed by this owner.");
        }
        contracts.removeIf(c -> c == contract);
        contract.drop();
    }

    public void drop() {
        contracts.stream().toList().forEach(Contract::drop);
    }

    @Override
    public String toString() {
        return "-- OWNER" + '\n' +
                "-- Name                 " + name + '\n' +
                "-- Surname              " + surname + '\n';
    }
}

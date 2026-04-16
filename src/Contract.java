import java.time.LocalDate;

public class Contract {

    private final LocalDate beginDate;
    private final LocalDate endDate;
    private final double paidAmount;
    private Property property;
    private Owner owner;

    public Contract(LocalDate beginDate, LocalDate endDate, double paidAmount, Property property, Owner owner) {
        super();

        if (property.getContracts().stream().anyMatch(contract ->
                contract.beginDate.isBefore(endDate) &&
                        contract.endDate.isAfter(beginDate)
        )) {
            throw new IllegalArgumentException("Can't create a contract for the ownership during a period of an another contract's ownership.");
        }

        this.beginDate = beginDate;
        this.endDate = endDate;
        this.paidAmount = paidAmount;
        this.property = property;
        this.owner = owner;
        property.addContract(this);
        owner.addContract(this);
    }

    public void drop() {
        if (property != null && property.getContracts().contains(this)) {
            property.dropContract(this);
        }
        if (owner != null && owner.getContracts().contains(this)) {
            owner.dropContract(this);
        }
        property = null;
        owner = null;
    }

    public Property getProperty() {
        return property;
    }

    public Owner getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "-- CONTRACT" + '\n' +
                "-- Begin Date           " + beginDate + '\n' +
                "-- End Date             " + endDate + '\n' +
                "-- Paid Amount          " + paidAmount + '\n' +
                "-- Property             "  + "\n    " + String.join("\n    ", property.toString().split("\n")) + '\n' +
                "-- Owner                "  + "\n    " + String.join("\n    ", owner.toString().split("\n")) + '\n';
    }
}

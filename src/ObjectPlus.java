import java.io.*;
import java.util.*;

public abstract class ObjectPlus {

    private static Map<Class<? extends ObjectPlus>, List<ObjectPlus>> extents = new HashMap<>();

    public ObjectPlus() {
        if (!extents.containsKey(this.getClass())) {
            extents.put(this.getClass(), new ArrayList<>());
        }
        extents.get(this.getClass()).add(this);
    }

    public static void print() {
        System.out.println("\n============ ALL EXTENTS ============\n");
        extents.forEach((key, value) -> {
            System.out.println("====== Extent Of " + key + " ======");
            value.forEach(System.out::println);
        });
        System.out.println("\n============ ___________ ============\n");
    }

    public static void clear() {
        extents.clear();
    }

    @SuppressWarnings("unchecked")
    public static<T extends ObjectPlus> List<T> get(Class<? extends ObjectPlus> forClass) throws ClassNotFoundException {
        List<ObjectPlus> classExtent = extents.get(forClass);
        if (classExtent == null) {
            throw new ClassNotFoundException("Couldn't find the class extent for the " + forClass.getName() + ".");
        }
        return classExtent.stream().map((ObjectPlus o) -> (T) forClass.cast(o)).toList();
    }

    protected Optional<List<ObjectPlus>> getExtent() {
        return Optional.ofNullable(extents.get(this.getClass()));
    }
}
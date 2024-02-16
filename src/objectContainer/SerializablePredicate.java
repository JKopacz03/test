package objectContainer;

import java.io.Serializable;

public interface SerializablePredicate<T> extends Serializable {
    boolean test(T t);
}

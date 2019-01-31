package Opt;


import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by fj on 2018/8/27.
 */

public final class Optional<T> {
    private static final Optional<?> EMPTY = new Optional();
    private final T value;

    private Optional() {
        this.value = null;
    }

    public static <T> Optional<T> empty() {
        Optional var0 = EMPTY;
        return var0;
    }

    private Optional(T var1) {
        this.value = Objects.requireNonNull(var1);
    }

    public static <T> Optional<T> of(T var0) {
        return new Optional(var0);
    }

    public static <T> Optional<T> ofNullable(T var0) {
        return var0 == null?empty():of(var0);
    }

    public T get() {
        if(this.value == null) {
            throw new NoSuchElementException("No value present");
        } else {
            return this.value;
        }
    }

    public boolean isPresent() {
        return this.value != null;
    }

    public void ifPresent(Consumer<? super T> var1) {
        if(this.value != null) {
            var1.accept(this.value);
        }

    }

    public Optional<T> filter(Predicate<? super T> var1) {
        Objects.requireNonNull(var1);
        return !this.isPresent()?this:(var1.test(this.value)?this:empty());
    }

    public <U> Optional<U> map(Function<? super T, ? extends U> var1) {
        Objects.requireNonNull(var1);
        return !this.isPresent()?empty():ofNullable(var1.apply(this.value));
    }

    public <U> Optional<U> flatMap(Function<? super T, Optional<U>> var1) {
        Objects.requireNonNull(var1);
        return !this.isPresent()?empty():(Optional) Objects.requireNonNull(var1.apply(this.value));
    }

    public T orElse(T var1) {
        return this.value != null?this.value:var1;
    }

    public T orElseGet(Supplier<? extends T> var1) {
        return this.value != null?this.value:var1.get();
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> var1) throws Throwable {
        if(this.value != null) {
            return this.value;
        } else {
            throw (Throwable)var1.get();
        }
    }

    public boolean equals(Object var1) {
        if(this == var1) {
            return true;
        } else if(!(var1 instanceof Optional)) {
            return false;
        } else {
            Optional var2 = (Optional)var1;
            return Objects.equals(this.value, var2.value);
        }
    }

    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    public String toString() {
        return this.value != null?String.format("Optional[%s]", new Object[]{this.value}):"Optional.empty";
    }
}

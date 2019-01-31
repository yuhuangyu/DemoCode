package com.test.test;


import android.os.Build;

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
    private T value;

    private Optional() {
        this.value = null;
    }

    public static <T> Optional<T> empty() {
        Optional var0 = EMPTY;
        return var0;
    }

    private Optional(T var1) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.value = Objects.requireNonNull(var1);
        }
    }

    public static <T> Optional<T> of(T var0) {
        return new Optional(var0);
    }

    public static <T> Optional<T> ofNullable(T var0) {
        return var0 == null? (Optional<T>) empty() :of(var0);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                var1.accept(this.value);
            }
        }

    }

    public Optional<T> filter(Predicate<? super T> var1) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(var1);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return !this.isPresent()?this: (Optional<T>) (var1.test(this.value) ? this : empty());
        }
        return null;
    }

    public <U> Optional<U> map(Function<? super T, ? extends U> var1) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(var1);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return !this.isPresent()? (Optional<U>) empty() :ofNullable(var1.apply(this.value));
        }
        return null;
    }

    public <U> Optional<U> flatMap(Function<? super T, Optional<U>> var1) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(var1);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return !this.isPresent()?empty():(Optional) Objects.requireNonNull(var1.apply(this.value));
            }
        }
        return null;
    }

    public T orElse(T var1) {
        return this.value != null?this.value:var1;
    }

    public T orElseGet(Supplier<? extends T> var1) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return this.value != null?this.value:var1.get();
        }
        return null;
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> var1) throws Throwable {
        if(this.value != null) {
            return this.value;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                throw (Throwable)var1.get();
            }
        }
        return null;
    }

    public boolean equals(Object var1) {
        if(this == var1) {
            return true;
        } else if(!(var1 instanceof Optional)) {
            return false;
        } else {
            Optional var2 = (Optional)var1;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return Objects.equals(this.value, var2.value);
            }
        }
        return false;
    }

    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hashCode(this.value);
        }
        return 0;
    }

    public String toString() {
        return this.value != null?String.format("Optional[%s]", new Object[]{this.value}):"Optional.empty";
    }
}

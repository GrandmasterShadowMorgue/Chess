package my.chess.logic;

import java.util.Objects;

public class V2<T> {
    public final T x,y;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        V2<?> v2 = (V2<?>) o;
        return Objects.equals(x, v2.x) &&
                Objects.equals(y, v2.y);
    }

    @Override
    public int hashCode() {

        return Objects.hash(x, y);
    }

    public V2(T x, T y) {
        this.x = x;
        this.y = y;
    }
}

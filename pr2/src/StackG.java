/**
 * Created by Lev on 02.02.14.
 */

import java.util.Stack;

/**
 * Моделирует стек.
 */
public class StackG<E> extends Stack<E>{
    public E top() {
        return peek();
    }
    public E next_to_top() {
        E temp = pop();
        E res = peek();
        push(temp);
        return res;
    }
}

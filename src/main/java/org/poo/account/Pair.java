package org.poo.account;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Pair<T, K> {

    T first;
    K second;

    public Pair(T first, K second) {
        this.first = first;
        this.second = second;
    }

}

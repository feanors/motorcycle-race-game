package Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class User implements Comparable<User>, Serializable {

    private static final long serialVersionUID = 1L;

    private String username = new String();
    private long score;

    public User(String name, long score) {
        username = name;
        this.score = score;
    }

    @Override
    public int compareTo(User o) {
        if (this.score > o.score ) {
            return -1;
        } else if (this.score < o.score) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Score: %s\n", username, score);
    }
}


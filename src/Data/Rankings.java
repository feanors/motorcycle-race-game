package Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Rankings implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<User> rankingsList;

    Rankings() {
        rankingsList = new ArrayList<>();
    }

    void addNewScore(String username, long score) {
        rankingsList.add(new User(username,score));
    }

    String getTopTenScore() {
        return this.toString();
    }

    @Override
    public String toString() {
        String s = "";
        Collections.sort(rankingsList);

        if (rankingsList.size() > 10) {
            for (int i = 0; i < 10; i++) {
                s += (i+1) + ") " + rankingsList.get(i).toString();
            }
        } else {
            for(User u : rankingsList) {
                s += (rankingsList.indexOf(u)+1) + ") " + u.toString();
            }
        }

        return s;
    }
}

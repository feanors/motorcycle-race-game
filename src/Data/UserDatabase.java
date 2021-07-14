package Data;
import java.io.*;
import java.util.*;

public class UserDatabase {

    private HashMap<String, String> userPassStore;
    private Rankings rankings;

    public UserDatabase() {

        userPassStore = new HashMap<>();
        rankings = new Rankings();

        try {
            FileInputStream databaseStream = new FileInputStream("userDatabase.db");
            ObjectInputStream databaseInput = new ObjectInputStream(databaseStream);

            userPassStore = (HashMap<String, String>) databaseInput.readObject();

            FileInputStream rankingsStream = new FileInputStream("userRankings.rank");
            ObjectInputStream rankingsInput = new ObjectInputStream(rankingsStream);

            rankings = (Rankings) rankingsInput.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String logIn(String username, String password){
        if (userPassStore.containsKey(username)) {
            if (userPassStore.get(username).equals(password)) {
                return username;
            } else {
                return null;
            }
        }
        return registerUser(username,password);
    }

    private String registerUser(String username, String password) {
        userPassStore.put(username,password);
        return username;
    }

    public String getTopTenRankings() {
        return rankings.getTopTenScore();
    }

    public void submitGameResult(String username, long score) {
        rankings.addNewScore(username, score);
    }

    public void storeData() {
        try {
            FileOutputStream dbOut = new FileOutputStream("userDatabase.db");
            ObjectOutputStream database = new ObjectOutputStream(dbOut);

            database.writeObject(userPassStore);
            database.close();

            FileOutputStream rankOut = new FileOutputStream("userRankings.rank");
            ObjectOutputStream ranks = new ObjectOutputStream(rankOut);

            ranks.writeObject(rankings);
            ranks.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

package kma.ir.kucherenko;

import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BooleanSearch {
    private HashMap<String, Integer> booleanMap;

    public BooleanSearch() {
        this.booleanMap = new HashMap<>();
    }

    public String[] validateQuery(String query) {
        try {
            Pattern p = Pattern.compile("^\\d+(\\s(and|or|not)\\s\\d+)*$");
            Matcher m = p.matcher(query.toLowerCase());
            if (!m.matches())
                throw new InvalidParameterSpecException("Invalid request syntax");
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        }
        return query.split("\\s+");
    }

    public void booleanSearch(String query) {
          String[] updatedQuery = validateQuery(query);
//          for (String queryWord : updatedQuery)




    }
}

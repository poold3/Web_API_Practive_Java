import java.util.*;
import java.net.URL;
import java.net.HttpURLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

class Pokemon {

    public static void main(String[] args) {
        System.out.println("Welcome to my OIT Coding Challenge!");
        try {
            //Construct HashSet of all Pokémon Types
            final List<String> pokemonTypesList = Arrays.asList("normal", "fire", "water", "grass", "flying", "fighting",
                    "poison", "electric", "ground", "rock", "psychic", "ice", "bug", "ghost", "steel", "dragon", "dark",
                    "fairy");
            final HashSet<String> pokemonTypes = new HashSet<>(pokemonTypesList);

            //Get userInputType
            Scanner myScanner = new Scanner(System.in);
            System.out.print("Enter a Pokemon Type: ");

            String userInputType = myScanner.nextLine();

            if (!pokemonTypes.contains(userInputType)) {
                myScanner.close();
                throw new RuntimeException(userInputType + " is not a valid pokemon type.");
            }
            //Set endpoint URL
            URL typeEndPoint = new URL(String.format("https://pokeapi.co/api/v2/type/%s/", userInputType));

            //Create connection
            HttpURLConnection conn = (HttpURLConnection) typeEndPoint.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Get Response Code
            if (conn.getResponseCode() != 200) {
                myScanner.close();
                throw new RuntimeException("Response Code from typeEndPoint: " + conn.getResponseCode());
            }

            //Save our response into a string
            StringBuilder typeResponse = new StringBuilder();
            Scanner typeResponseScanner = new Scanner(typeEndPoint.openStream());

            while (typeResponseScanner.hasNext()) {
                typeResponse.append(typeResponseScanner.nextLine());
            }
            typeResponseScanner.close();

            //Parse the string into a json object
            JSONParser parse = new JSONParser();
            JSONObject data_obj = (JSONObject) parse.parse(typeResponse.toString());

            JSONArray pokemon = (JSONArray) data_obj.get("pokemon");

            //Build HashMap containing the name of each Pokémon and its endpoint
            HashMap<String,String> nameUrl = new HashMap<>();
            System.out.println();
            for (Object o : pokemon) {
                JSONObject curPokemon = (JSONObject) ((JSONObject) o).get("pokemon");
                System.out.println((String) curPokemon.get("name"));
                nameUrl.put((String) curPokemon.get("name"), (String) curPokemon.get("url"));
            }

            //Get the name of a Pokémon
            System.out.print("\nEnter a name from above: ");

            String userInputName = myScanner.nextLine();
            myScanner.close();

            //Ensure that the userInputName is of the correct type
            if (!nameUrl.containsKey(userInputName)) {
                throw new RuntimeException(userInputName + " is not a pokemon name listed above.");
            }

            //Set endpoint URL
            URL nameEndPoint = new URL(nameUrl.get(userInputName));

            //Create connection
            conn = (HttpURLConnection) nameEndPoint.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Get Response Code
            if (conn.getResponseCode() != 200) {
                myScanner.close();
                throw new RuntimeException("Response Code from nameEndPoint: " + conn.getResponseCode());
            }

            //Save our response into a string
            StringBuilder nameResponse = new StringBuilder();
            Scanner nameResponseScanner = new Scanner(nameEndPoint.openStream());

            while (nameResponseScanner.hasNext()) {
                nameResponse.append(nameResponseScanner.nextLine());
            }
            nameResponseScanner.close();

            //Parse the string into a json object
            parse = new JSONParser();
            data_obj = (JSONObject) parse.parse(nameResponse.toString());

            //Output Pokémon Information
            System.out.printf("Name: %s\n",data_obj.get("name"));
            System.out.printf("Height: %s\n",data_obj.get("height"));
            System.out.printf("Weight: %s\n",data_obj.get("weight"));
            System.out.print("Games: \n");
            JSONArray games = (JSONArray) data_obj.get("game_indices");
            for (Object game : games) {
                JSONObject index = (JSONObject) game;
                System.out.printf("\t%s\n", ((JSONObject) index.get("version")).get("name"));
            }
            System.out.print("Moves: \n");
            JSONArray moves = (JSONArray) data_obj.get("moves");
            for (Object move : moves) {
                JSONObject index = (JSONObject) move;
                System.out.printf("\t%s\n", ((JSONObject) index.get("move")).get("name"));
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}

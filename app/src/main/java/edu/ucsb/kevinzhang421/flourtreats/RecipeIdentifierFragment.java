package edu.ucsb.kevinzhang421.flourtreats;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeIdentifierFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeIdentifierFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private DataLoader dataLoader;

    public RecipeIdentifierFragment() {
    }

    public static RecipeIdentifierFragment newInstance(String param1, String param2) {
        RecipeIdentifierFragment fragment = new RecipeIdentifierFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_identifier, container, false);
    }

    public void onStart() {
        super.onStart();
        final DataLoader dataLoader = new DataLoader(3);
        InputStream inputStream = getResources().openRawResource(R.raw.recipes);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        ArrayList<Recipe> recipes = dataLoader.getRecipes();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals("")) {
                    line = Ingredient.removeTextSurroundedBy("(", ")", line);
                    line = Ingredient.removeTextSurroundedBy("[", "]", line);
                    line = Ingredient.removeTextSurroundedBy("{", "}", line);
                    line = Ingredient.removeTextSurroundedBy("<", ">", line);
                    String[] ingredients = line.split(",");
                    Recipe recipe = new Recipe();
                    recipe.setName(ingredients[0]);
                    recipe.setURL(ingredients[1]);               //NEW STUFF
                    for (int i = 2; i < ingredients.length; i++) {
                        Ingredient ingredient = new Ingredient(ingredients[i]);
                        String ingredientName = ingredient.getIngredient();
                        if (!ingredientName.equals("")) {
                            recipe.addIngredient(ingredient);
                        }
                    }
                    recipe.calculateRatios();
                    recipes.add(recipe);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Button btn = (Button) getView().findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ingredientsEditText = (EditText) getView().findViewById(R.id.ingredientsEditText);
                TextView alternateResultsTextView = (TextView) getView().findViewById(R.id.alternateResultsTextView);
                TextView resultTextView = (TextView) getView().findViewById((R.id.resultTextView));
                String bigPhrase = ingredientsEditText.getText().toString();


                Recipe unknownRecipe = dataLoader.convertStringToRecipe(bigPhrase);

                ArrayList<Recipe> closestRecipes = dataLoader.getKNearestNeighborRecipes(unknownRecipe);
                String closestRecipeList = closestRecipes.get(0).getName() + ": " + closestRecipes.get(0).getURL();
                String alternateRecipes = "";
                if (closestRecipes.size() > 1) {
                    alternateRecipes += "similar to:";
                }
                for (int i = 1; i < closestRecipes.size(); i++) {
                    alternateRecipes += "\n" + closestRecipes.get(i).getName() + ": " + closestRecipes.get(i).getURL();
                }

                resultTextView.setText(closestRecipeList);
                alternateResultsTextView.setText(alternateRecipes);

            }
        });
        Button clearButton = getView().findViewById(R.id.clearRecipesButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ingredientsEditText = (EditText) getView().findViewById(R.id.ingredientsEditText);
                TextView alternateResultsTextView = (TextView) getView().findViewById(R.id.alternateResultsTextView);
                TextView resultTextView = (TextView) getView().findViewById((R.id.resultTextView));
                ingredientsEditText.setText("");
                alternateResultsTextView.setText("");
                resultTextView.setText("");
            }
        });
    }

}
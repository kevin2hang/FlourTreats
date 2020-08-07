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
 * Use the {@link SearchRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchRecipesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SearchRecipesFragment() {
    }


    public static SearchRecipesFragment newInstance(String param1, String param2) {
        SearchRecipesFragment fragment = new SearchRecipesFragment();
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
        return inflater.inflate(R.layout.fragment_search_recipes, container, false);
    }

    public void onStart() {
        super.onStart();
        Button findARecipeBtn = (Button) getView().findViewById(R.id.findARecipeBtn);

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
                    recipe.setURL(ingredients[1]);
                    recipes.add(recipe);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        findARecipeBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                EditText recipeNameEditText = (EditText) getView().findViewById(R.id.recipeNameEditTxt);
                TextView displayTxtView = (TextView) getView().findViewById(R.id.displayTxtView);
                String searchTerms[] = recipeNameEditText.getText().toString().toLowerCase().split(" ");
                String recipesToBeDisplayed = "";

                ArrayList<Recipe> recipes = dataLoader.getRecipes();
                ArrayList<Recipe> similarGoods = new ArrayList<>();
                ArrayList<Integer> numOfSearchTermsInGood = new ArrayList<>();
                for (int i = 0; i < recipes.size(); i++) {
                    Recipe recipe = recipes.get(i);
                    int numOfSearchTerms = 0;
                    for (int j = 0; j < searchTerms.length; j++) {
                        if (recipe.getName().toLowerCase().contains(searchTerms[j])) {
                            numOfSearchTerms++;
                        }
                    }
                    if (similarGoods.size() == 0 && numOfSearchTerms > 0) {
                        similarGoods.add(recipe);
                        numOfSearchTermsInGood.add(numOfSearchTerms);
                    } else if (numOfSearchTerms > 0) {
                        boolean hasBeenAdded = false;
                        for (int j = 0; j < similarGoods.size(); j++) {
                            if (numOfSearchTerms > numOfSearchTermsInGood.get(j)) {
                                numOfSearchTermsInGood.add(j, numOfSearchTerms);
                                similarGoods.add(j, recipe);
                                hasBeenAdded = true;
                                break;
                            }
                        }
                        if (!hasBeenAdded) {
                            numOfSearchTermsInGood.add(numOfSearchTerms);
                            similarGoods.add(recipe);
                        }
                    }
                }

                if (similarGoods.size() == 0)
                    recipesToBeDisplayed = "No " + recipeNameEditText.getText().toString() + " recipes in database";
                else {
                    for (int i = 0; i < similarGoods.size(); i++) {
                        Recipe recipe = similarGoods.get(i);
                        recipesToBeDisplayed += recipe.getName() + ": " + recipe.getURL() + "\n";
                    }
                }
                displayTxtView.setText(recipesToBeDisplayed);
            }
        });
        Button resetBtn2 = (Button) getView().findViewById(R.id.resetBtn2);
        resetBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText recipeNameEditText = (EditText) getView().findViewById(R.id.recipeNameEditTxt);
                TextView displayTxtView = (TextView) getView().findViewById((R.id.displayTxtView));
                recipeNameEditText.setText("");
                displayTxtView.setText("");
            }
        });

    }

}
package jimmy.mcgymmy.ui;

import static jimmy.mcgymmy.commons.util.CollectionUtil.requireAllNonNull;

import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;

/**
 * Displays a pie chart that gives statistic about the total macronutrient values of the food items in McGymmy.
 */
public class SummaryDisplay extends UiPart<AnchorPane> {
    private static final String FXML = "SummaryDisplay.fxml";
    private static final String TOTAL_CALORIES_TEXT = "Total Calories %d";
    private static final String TOTAL_PROTEIN_TEXT = "Protein: %d";
    private static final String TOTAL_CARBOHYDRATE_TEXT = "Carbs: %d";
    private static final String TOTAL_FAT_TEXT = "Fats: %d";

    @FXML
    private PieChart pieChart;

    /**
     * Creates a summary display.
     * Contains summary of the items shown.
     */
    public SummaryDisplay() {
        super(FXML);
        pieChart.setAnimated(false);
        pieChart.setLabelsVisible(false);
        pieChart.setLegendSide(Side.RIGHT);
    }

    public void setTotalMacronutrients(long totalCalories, long totalProtein, long totalCarbs, long totalFats) {
        requireAllNonNull(totalCalories, totalProtein, totalCarbs, totalFats);
        assert totalCalories > 0 : "Total Calories is negative";
        pieChart.setTitle(String.format(TOTAL_CALORIES_TEXT, totalCalories));

        //Reset the data
        pieChart.getData().clear();

        //Update the data to the correct value
        addData(TOTAL_CARBOHYDRATE_TEXT, totalCarbs);
        addData(TOTAL_PROTEIN_TEXT, totalProtein);
        addData(TOTAL_FAT_TEXT, totalFats);
    }

    private void addData(String formatString, long count) {

        //If the count for the data is <= 0, do not show the data value
        if (count <= 0) {
            return;
        }

        //Add the data to the chart
        pieChart.getData().add(new PieChart.Data(String.format(formatString, count), count));
    }
}

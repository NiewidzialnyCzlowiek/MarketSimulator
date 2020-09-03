package io.bartlomiejszal.marketsimulator.views;

import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Fund;
import io.bartlomiejszal.marketsimulator.controllers.FundCellController;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

public final class FundListViewCell extends ListCell<Fund> {
    private final FundCellController fundCellController = new FundCellController();
    private final Node cellView = fundCellController.getCellView();

    @Override
    public void updateItem(Fund fund, boolean empty) {
        super.updateItem(fund, empty);
        if((fund != null) && !empty) {
            fundCellController.setFund(fund);
            setGraphic(cellView);
        }
        else
            setGraphic(null);
    }
}

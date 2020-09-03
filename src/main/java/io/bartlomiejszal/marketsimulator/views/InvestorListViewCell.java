package io.bartlomiejszal.marketsimulator.views;

import io.bartlomiejszal.marketsimulator.model.usersAndCustomers.Investor;
import io.bartlomiejszal.marketsimulator.controllers.InvestorCellController;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

public final class InvestorListViewCell extends ListCell<Investor> {
    private final InvestorCellController investorCellController = new InvestorCellController();
    private final Node cellView = investorCellController.getCellView();

    @Override
    public void updateItem(Investor investor, boolean empty) {
        super.updateItem(investor, empty);
        if((investor != null) && !empty) {
            investorCellController.setInvestor(investor);
            setGraphic(cellView);
        }
        else
            setGraphic(null);
    }
}

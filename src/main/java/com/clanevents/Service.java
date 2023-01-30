package com.clanevents;

import lombok.Data;

import javax.swing.event.SwingPropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Data
public class Service {

    SwingPropertyChangeSupport propChangeFirer;
    Optional<List<List<Object>>> apiValues = Optional.empty();
    List<SheetValueRange> sheetValueRangeList = new ArrayList<>();
    State state = State.IDLE;
    public Service() {
        propChangeFirer = new SwingPropertyChangeSupport(this);
    }

    public void addListener(PropertyChangeListener prop) {
        propChangeFirer.addPropertyChangeListener(prop);
    }

    public void setState(State newState) {
        State oldVal = this.state;
        this.state = newState;
        propChangeFirer.firePropertyChange("state", oldVal, newState);
    }
    public void refreshData() {
        setState(State.LOADING);
        CompletableFuture.supplyAsync(() -> {
            try {
                List<SheetValueRange> googleValues = GoogleSheet.getValues();
                setSheetValueRangeList(googleValues);
            } catch (Exception e){
                e.printStackTrace();
                return State.ERROR;
            }
            return State.COMPLETED;
        }).<State>handle((r, v) -> {
            setState(r);
            return null;
        });
    }

    public Optional<List<List<Object>>> getSheet(String header){
        return sheetValueRangeList.stream()
                .filter(s -> s.getSheet().equals(header))
                .findFirst()
                .map(m -> m.getValueRange().getValues());
    }
}
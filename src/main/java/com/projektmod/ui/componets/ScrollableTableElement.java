package com.projektmod.ui.componets;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ObjectSelectionList;

import java.util.ArrayList;
import java.util.List;

/*
Container for the @ref ObjectSelectionList that autoaligns items according to the column definition
 */
abstract public class ScrollableTableElement<E extends ObjectSelectionList.Entry<E>> extends ObjectSelectionList.Entry<E>{
    private ArrayList<AbstractWidget> elements = new ArrayList<>();
    private Columns columns;

    public ScrollableTableElement(Columns columns){
        this.columns = columns;
    }

    public void addElement(AbstractWidget widget) {
        this.elements.add(widget);
    }

    @Override
    public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
        int xPlacement = 0;
        for (int i = 0; i < columns.lengths.size(); i++){
            AbstractWidget widget = elements.get(i);
            int colWidth = columns.lengths.get(i);
            widget.setWidth(colWidth);

            widget.setX(this.getX() + xPlacement);
            widget.setY(this.getY());

            widget.extractRenderState(graphics, mouseX, mouseY, a);
        }
    }

    public static class Columns{
        public Columns(List<Integer> c){
            lengths = (ArrayList<Integer>) c;
        }

        private ArrayList<Integer> lengths = new ArrayList<Integer>();

        public ArrayList<Integer> getLengths() {
            return lengths;
        }

        public void setLengths(ArrayList<Integer> lengths) {
            this.lengths = lengths;
        }

    }
}

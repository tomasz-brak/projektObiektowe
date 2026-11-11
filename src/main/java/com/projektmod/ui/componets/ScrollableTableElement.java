package com.projektmod.ui.componets;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ObjectSelectionList;

import java.util.ArrayList;
import java.util.List;

public abstract class ScrollableTableElement<E extends ObjectSelectionList.Entry<E>> extends ObjectSelectionList.Entry<E> {
    private final List<AbstractWidget> elements = new ArrayList<>();
    private final Columns columns;

    public ScrollableTableElement(Columns columns) {
        this.columns = columns;
    }

    public void addElement(AbstractWidget widget) {
        this.elements.add(widget);
    }

    public void setColumnSpacing(int spacing) {
        this.columns.columnSpacing = spacing;
    }

    @Override
    public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
        int xOffset = this.columns.columnSpacing;
        List<Integer> lengths = columns.getLengths();
        int count = Math.min(elements.size(), lengths.size());

        for (int i = 0; i < count; i++) {
            AbstractWidget widget = elements.get(i);
            int colWidth = lengths.get(i);

            widget.setWidth(colWidth);

            int yPos = this.getY() + Math.max(0, (this.getHeight() - widget.getHeight()) / 2);

            widget.setX(this.getX() + xOffset);
            widget.setY(yPos);

            widget.extractRenderState(graphics, mouseX, mouseY, a);

            xOffset += colWidth + this.columns.columnSpacing;
        }
    }

    public static class Columns {
        private List<Integer> lengths;
        private int columnSpacing = 4;

        public Columns(List<Integer> lengths) {
            this.lengths = new ArrayList<>(lengths);
        }

        public List<Integer> getLengths() {
            return lengths;
        }

        public void setLengths(List<Integer> lengths) {
            this.lengths = new ArrayList<>(lengths);
        }

        public int getAggregateWidth(){
            return lengths.stream().reduce(0, (a, b) -> {return a + b + columnSpacing;}) + columnSpacing;
        }
    }
}
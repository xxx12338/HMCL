/*
 * Hello Minecraft! Launcher.
 * Copyright (C) 2017  huangyuhui <huanghongxun2008@126.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */
package org.jackhuang.hmcl.ui.animation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.jackhuang.hmcl.ui.FXUtils;

public final class TransitionHandler implements AnimationHandler {
    private final StackPane view;
    private Timeline animation;
    private Duration duration;
    private Node previousNode, currentNode;

    /**
     * @param view A stack pane that contains another control that is {@link Parent}
     */
    public TransitionHandler(StackPane view) {
        this.view = view;
        currentNode = view.getChildren().stream().findFirst().orElse(null);
    }

    @Override
    public Node getPreviousNode() {
        return previousNode;
    }

    @Override
    public Node getCurrentNode() {
        return currentNode;
    }

    @Override
    public StackPane getCurrentRoot() {
        return view;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    public void setContent(Node newView, AnimationProducer transition) {
        setContent(newView, transition, Duration.millis(320));
    }

    public void setContent(Node newView, AnimationProducer transition, Duration duration) {
        this.duration = duration;

        Timeline prev = animation;
        if (prev != null)
            prev.stop();

        updateContent(newView);

        Timeline nowAnimation = new Timeline();
        nowAnimation.getKeyFrames().addAll(transition.animate(this));
        nowAnimation.getKeyFrames().add(new KeyFrame(duration, e -> {
            view.getChildren().remove(previousNode);
        }));
        nowAnimation.play();
        animation = nowAnimation;
    }

    private void updateContent(Node newView) {
        if (view.getWidth() > 0 && view.getHeight() > 0) {
            previousNode = currentNode;
            if (previousNode == null)
                previousNode = NULL;
        } else
            previousNode = NULL;

        if (previousNode == currentNode)
            previousNode = NULL;

        currentNode = newView;

        view.getChildren().setAll(previousNode, currentNode);
    }

    private static final StackPane NULL = new StackPane();
}

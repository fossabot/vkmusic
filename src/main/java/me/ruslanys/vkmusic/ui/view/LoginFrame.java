package me.ruslanys.vkmusic.ui.view;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.springframework.stereotype.Component;

import javax.swing.*;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
@Component
public class LoginFrame extends LoadingFrame implements ChangeListener<Worker.State> {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";

    private WebView webView;


    @Override
    protected void initWindow() {
        setSize(1000, 660);
        setLocationRelativeTo(null);

        setTitle("Авторизация");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    protected JComponent initMainPanel() {
        JFXPanel panel = new JFXPanel();
        Platform.runLater(() -> {
            webView = new WebView();
            WebEngine engine = webView.getEngine();
            engine.setJavaScriptEnabled(true);
            engine.setUserAgent(USER_AGENT);

            addChangeListener(LoginFrame.this);
            panel.setScene(new Scene(webView));
        });
        return panel;
    }

    public void addChangeListener(ChangeListener<Worker.State> changeListener) {
        Platform.runLater(() -> webView.getEngine().getLoadWorker().stateProperty().addListener(changeListener));
    }

    @Override
    public void changed(ObservableValue<? extends Worker.State> ov, Worker.State oldState, Worker.State newState) {
        switch (newState) {
            case SCHEDULED:
            case RUNNING:
                setState(State.LOADING);
                break;
            default:
                setState(State.MAIN);
        }
    }

    public void load(String url) {
        Platform.runLater(() -> {
            setState(State.LOADING);
            webView.getEngine().load(url);
        });
    }

}

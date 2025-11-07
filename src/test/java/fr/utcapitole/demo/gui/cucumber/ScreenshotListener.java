package fr.utcapitole.demo.gui.cucumber;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.Result;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestStepFinished;

public class ScreenshotListener implements ConcurrentEventListener {
    private static final String screenshotDirectory = "./target";

    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepFinished.class, this::testStepFinished);
    }

    private void testStepFinished(TestStepFinished event) {
        Result result = event.getResult();
        if (!Status.PASSED.equals(result.getStatus())) {
            System.out.println("FAILED");
            // TODO: take a snapshot
        }
    }
}

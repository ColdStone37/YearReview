package yearreview.app.render;

import yearreview.app.config.GlobalSettings;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * A class that manages the renderprocess by generating frames and allowing the {@link yearreview.app.render.RenderWorker} to get next frame for rendering.
 *
 * @author ColdStone37
 */
public class RenderManager {
	private ConcurrentLinkedDeque<RenderWorker> waitingWorkers;
	private AtomicBoolean waitingForWorker;


	public RenderManager() {
		waitingForWorker = new AtomicBoolean();
		workers = new ArrayList<RenderWorker>();
		for (int i = 0; i < GlobalSettings.getRenderThreads(); i++)
			workers.add(new RenderWorker());
	}

	public void setWaiting(RenderWorker rw) {
		waitingWorkers.offer(rw);
		if (waitingForWorker.getAndSet(false))
			notify();
	}

	private void runJob(RenderJob job) throws InterruptedException {
		if (waitingWorkers.isEmpty()) {
			waitingForWorker.set(true);
			wait();
		}
		waitingWorkers.poll().assignJob(job);
	}
}
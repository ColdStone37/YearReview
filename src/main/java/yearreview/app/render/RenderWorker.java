package yearreview.app.render;

/**
 * A class that peforms {@link yearreview.app.render.RenderJob RenderJobs} on a seperate thread.
 *
 * @author ColdStone37
 */
public class RenderWorker extends Thread {
	private static int workerCount = 0;
	private final int workerId;

	private RenderManager renderManager;
	private RenderJob assignedJob = null;

	public RenderWorker(RenderManager rm) {
		renderManager = rm;
		workerId = workerCount;
		workerCount++;
	}

	@Override
	public void run() {
		System.out.println("Worker " + workerId + " running");
		try {
			renderManager.setWaiting(this);
			wait();
			while (assignedJob != null) {
				doJob(assignedJob);
				renderManager.setWaiting(this);
				wait();
			}
			System.out.println("Worker " + workerId + " finished");
		} catch (InterruptedException e) {
			System.out.println("Worker " + workerId + " interrupted");
		}
	}

	private void doJob(RenderJob rj) {
		//TODO
	}

	public void assignJob(RenderJob rj) {
		assignedJob = rj;
		notify();
	}
}
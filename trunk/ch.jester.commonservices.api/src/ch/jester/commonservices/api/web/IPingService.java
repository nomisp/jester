package ch.jester.commonservices.api.web;

public interface IPingService {
	public final static int REACHABLE = 0;
	public final static int NOT_REACHABLE = -1;
	public int ping(String pInetAddress, int pTimeOut);
	public int ping(String pInetAddress, int pTimeOut, int pReschedule);
}

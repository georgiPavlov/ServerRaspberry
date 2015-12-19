/**
 * Created by georgipavlov on 19.12.15.
 */
public interface Device {
    boolean openStream(int pos);
    void pauseStream();
    void continueStream();
    void forwardStream();
    void backwardStream();

}

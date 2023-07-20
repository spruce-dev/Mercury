import com.mercury.application.TestGame;
import dev.spruce.mercury.Window;
import dev.spruce.mercury.application.ApplicationConfig;
import dev.spruce.mercury.application.ApplicationConfigFactory;

public class Main {

    public static void main(String[] args){
        ApplicationConfig config = ApplicationConfigFactory.create()
                .setTitle("Test Game | Mercury")
                .setDisplaySize(1600, 900)
                .setResizable(true)
                .setStartMaximized(true)
                .enableVSync();
        TestGame application = new TestGame(config);

        Window window = new Window(application);
        window.run();
    }
}

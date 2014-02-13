package net.ninjacat.pop500.config.modules;

import net.ninjacat.drama.ActorSystem;
import net.ninjacat.drama.ActorSystemFactory;
import net.ninjacat.dws.DefaultContext;
import net.ninjacat.dws.WebService;
import net.ninjacat.pop500.api.actors.BitmapApiResponseActor;
import net.ninjacat.pop500.api.net.BitmapService;
import net.ninjacat.pop500.api.net.JsonService;
import org.microba.core.binding.Binder;

import java.util.HashSet;

public class WebModule implements InjectionModule {
    @Override
    public void configure(Binder binder) {
        ActorSystem mainActorSystem = createMainActorSystem();
        binder.bind(ActorSystem.class).toInstance(mainActorSystem);
    }

    private ActorSystem createMainActorSystem() {
        ActorSystemFactory factory = new ActorSystemFactory();
        ActorSystem mainActorSystem = factory.create();

        DefaultContext context = new DefaultContext();
        context.setNumberOfWorkers(Runtime.getRuntime().availableProcessors() + 2);

        mainActorSystem.createActor(WebService.class, WebService.DEFAULT_NAME, context);
        mainActorSystem.createActor(JsonService.class, "JsonService");
        mainActorSystem.createActor(BitmapService.class, "BitmapService");
        mainActorSystem.createActor(BitmapApiResponseActor.class, "asd", new HashSet<String>());

        return mainActorSystem;
    }
}

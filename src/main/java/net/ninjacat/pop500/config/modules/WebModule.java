package net.ninjacat.pop500.config.modules;

import net.ninjacat.drama.ActorSystem;
import net.ninjacat.drama.ActorSystemFactory;
import net.ninjacat.dws.DefaultContext;
import net.ninjacat.dws.WebService;
import net.ninjacat.pop500.api.net.JsonService;
import org.microba.core.binding.Binder;

public class WebModule implements InjectionModule {
    @Override
    public void configure(Binder binder) {
        binder.bind(ActorSystem.class).toInstance(createMainActorSystem());
    }

    private ActorSystem createMainActorSystem() {
        ActorSystemFactory factory = new ActorSystemFactory();
        ActorSystem mainActorSystem = factory.create();

        DefaultContext context = new DefaultContext();
        context.setNumberOfWorkers(Runtime.getRuntime().availableProcessors() + 2);

        mainActorSystem.createActor(WebService.class, "WebService", context);
        mainActorSystem.createActor(JsonService.class, "JsonService");

        return mainActorSystem;
    }
}

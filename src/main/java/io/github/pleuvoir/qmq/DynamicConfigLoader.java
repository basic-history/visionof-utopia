package io.github.pleuvoir.qmq;
import java.util.ServiceLoader;
public final class DynamicConfigLoader {
    // TODO(keli.wang): can we set this using config?
    private static final DynamicConfigFactory FACTORY;

    static {
        ServiceLoader<DynamicConfigFactory> factories = ServiceLoader.load(DynamicConfigFactory.class);
        DynamicConfigFactory instance = null;
        for (DynamicConfigFactory factory : factories) {
            instance = factory;
            break;
        }

        FACTORY = instance;
    }

    private DynamicConfigLoader() {
    }

    public static DynamicConfig load(final String name) {
        return load(name, true);
    }

    public static DynamicConfig load(final String name, final boolean failOnNotExist) {
        return FACTORY.create(name, failOnNotExist);
    }
}
package com.jayfella.importer.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * The service manager holds single instances of service such as JmeEngineService.
 * Allows us to easily access these services from various areas of the SDK without having to pass references.
 */
public class ServiceManager {

    private static final Logger log = Logger.getLogger(ServiceManager.class.getName());

    private static final ConcurrentHashMap<Class<? extends Service>, Service> services = new ConcurrentHashMap<>();

    /**
     * Returns the instance of the given Service class, or null if none exists.
     * @param serviceClass the service class desired.
     * @param <T>          the service class desired.
     * @return the instance of the given Service class, or null if none exists.
     */
    public static <T extends Service> T getService(Class<T> serviceClass) {
        // return (T)services.get(serviceClass);

        Service service = services.entrySet().stream()
                .filter(entry -> isAssignable(entry.getValue().getClass(), serviceClass))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);


        if (service != null) {

            // Every service logs a ThreadId that should be used when using the service.
            // OR -1 to ignore this check. We ignore this check for the JmeEngineService because we want to call enqueue
            // from anywhere. Engine threading is something the user should already be aware of.
            // For any other service it's important that they only be accessed from the thread they were created on.

            long threadId = service.getThreadId();

            // According to the javadoc thread IDs are positive numbers, so any negative number = ignore this check.
            if (threadId > -1) {

                long currentThreadId = Thread.currentThread().getId();

                // for now we'll leave it as a warning. If the system proves useful we'll up the ante to an exception.
                if (currentThreadId != threadId) {
                    log.warning("Service '" + serviceClass.getSimpleName() + "' was accessed by the wrong thread: " + Thread.currentThread().getName());
                    // throw new ServiceManagerThreadingException("Service '" + serviceClass.getSimpleName() + "' was accessed by the wrong thread: " + Thread.currentThread().getName());
                    log.warning(Arrays.toString(Thread.currentThread().getStackTrace()).replace( ',', '\n' ));
                }

            }
        }

        return (T) service;
    }

    /**
     * Registers the given Service class with the given constructor values.
     * @param serviceClass      the Service class to register.
     * @param constructorValues the values to pass to the constructor
     * @param <T>               the Service class to register.
     * @return the constructed Service instance.
     */
    public static <T extends Service> T registerService(Class<T> serviceClass, Object... constructorValues) {

        Class<?>[] parameterTypes = new Class<?>[constructorValues.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            parameterTypes[i] = constructorValues[i].getClass();
        }

        try {
            Constructor<T> constructor = serviceClass.getConstructor(parameterTypes);
            T service = constructor.newInstance(constructorValues);
            services.put(serviceClass, service);

            log.info("Registered Service: " + serviceClass.getSimpleName());
            return service;

        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Registers the given service class using the noArgs constructor.
     * @param serviceClass the Service class to register.
     * @param <T>          the Service class to register.
     * @return the constructed instance of the given Service class.
     */
    public static <T extends Service> T registerService(Class<T> serviceClass) {

        try {
            Constructor<T> constructor = serviceClass.getConstructor();
            T service = constructor.newInstance();
            services.put(serviceClass, service);

            log.info("Registered Service: " + serviceClass.getSimpleName());
            return service;

        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Adds the already constructed service to the ServiceManager.
     * @param service the Service to add.
     * @param <T>     the Service Class type.
     * @return the given service.
     */
    public static <T extends Service> T registerService(Service service) {
        return (T) services.put(service.getClass(), service);
    }

    public static void stop() {
        services.values().forEach(Service::stop);
    }

    private static boolean isAssignable(Class<?> clazz, Class<?> typeClass) {

        while (clazz != null) {

            if (clazz.isAssignableFrom(typeClass)) {
                return true;
            }

            Class<?>[] interfaces = clazz.getInterfaces();

            for (Class<?> interfaceClass : interfaces) {

                // lots of things implement savable. We don't want to match that.
                if (interfaceClass == Service.class) {
                    continue;
                }

                if (interfaceClass.isAssignableFrom(typeClass)) {
                    return true;
                }
            }

            clazz = clazz.getSuperclass();

            // everything is assignable from object.
            if (clazz == Object.class) {
                break;
            }
        }

        return false;
    }

}

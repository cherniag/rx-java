package gc.learn.rxjava.core;

import rx.Observable;
import rx.Subscription;
import sun.rmi.runtime.Log;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;


public class ObservableL {
    public static void main(String[] args) throws InterruptedException {
        just();
        empty();
        never();
        error();
        defer();
        create();
        // Observable.interval
        // Observable.range
        // Observable.timer
        from();
    }

    private static void just() {
        System.out.println('\n' + "::justObservable::");
        // creates Observable which just passes values and invokes onComplete
        Observable<Integer> justObservable = Observable.just(1, 2, 3, 4);

        Subscription subscription = justObservable.subscribe(System.out::println,    // onNext
                e -> System.out.println("error:: " + e), // onError
                () -> System.out.println("complete")     // onComplete
        );
        System.out.println("subscription.isUnsubscribed() = " + subscription.isUnsubscribed());

        // the same scenarion with new subscription
        System.out.println("Subscribe 2");
        Subscription subscription2 = justObservable.subscribe(System.out::println,    // onNext
                e -> System.out.println("subscription2 error:: " + e), // onError
                () -> System.out.println("subscription2 complete")     // onComplete
        );
    }

    private static void empty() {
        System.out.println('\n' + "::emptyObservable::");
        // creates Observable which just invokes onComplete
        Observable<Integer> emptyObservable = Observable.empty();

        Subscription subscription = emptyObservable.subscribe(System.out::println,    // onNext
                e -> System.out.println("error:: " + e), // onError
                () -> System.out.println("complete")     // onComplete
        );
        System.out.println("subscription.isUnsubscribed() = " + subscription.isUnsubscribed());
    }

    private static void never() {
        System.out.println('\n' + "::neverObservable::");
        // creates Observable which will never invoke
        Observable<Integer> neverObservable = Observable.never();

        Subscription subscription = neverObservable.subscribe(System.out::println,    // onNext
                e -> System.out.println("error:: " + e), // onError
                () -> System.out.println("complete")     // onComplete
        );
        System.out.println("subscription.isUnsubscribed() = " + subscription.isUnsubscribed());
    }

    private static void error() {
        System.out.println('\n' + "::errorObservable::");
        // creates Observable which will only invoke onError
        Observable<Integer> errorObservable = Observable.error(new RuntimeException("Oops"));

        Subscription subscription = errorObservable.subscribe(System.out::println,    // onNext
                e -> System.out.println("error:: " + e), // onError
                () -> System.out.println("complete")     // onComplete
        );
        System.out.println("subscription.isUnsubscribed() = " + subscription.isUnsubscribed());
    }

    private static void defer() throws InterruptedException {
        System.out.println('\n' + "::defer::");
        // Defines observable which will be created at subscription moment
        Observable<Long> defer = Observable.defer(() -> Observable.just(System.currentTimeMillis()));

        // creates Observable.just
        defer.subscribe(System.out::println);
        Thread.sleep(1000);
        // creates new Observable.just
        defer.subscribe(System.out::println);
    }

    private static void create() throws InterruptedException {
        System.out.println('\n' + "::create::");
        Observable<Integer> created = Observable.create(subscriber -> { // onSubscribe listener
            subscriber.onNext(5);
            subscriber.onCompleted();
        });

        // invokes onSubscribe listener
        created.subscribe(System.out::println,
                e -> System.out.println("error:: " + e),
                () -> System.out.println("complete")
        );

        System.out.println('\n' + "::the same as defer::");
        Observable<Long> asDefer = Observable.create(subscriber -> { // onSubscribe listener
            subscriber.onNext(System.currentTimeMillis());
            subscriber.onCompleted();
        });
        asDefer.subscribe(System.out::println);
        Thread.sleep(1000);
        asDefer.subscribe(System.out::println);
    }

    public static void from() {
        System.out.println('\n' + "::from futureTask::");
        FutureTask<Integer> futureTask = new FutureTask<>(() -> {
            Thread.sleep(2000);
            return 100;
        });
        new Thread(futureTask).start();

        Observable<Integer> from = Observable.from(futureTask);
        System.out.println(System.currentTimeMillis() + ": subscribe");
        from.subscribe(
                i -> System.out.println(System.currentTimeMillis() + ": onNext::" + i),
                e -> System.out.println(System.currentTimeMillis() + ": onError:: " + e),
                () -> System.out.println(System.currentTimeMillis() + ": onComplete")
        );

        System.out.println('\n' + "::from futureTask with timeout::");
        futureTask = new FutureTask<>(() -> {
            Thread.sleep(2000);
            return 100;
        });
        new Thread(futureTask).start();

        Observable<Integer> fromWithTimeout = Observable.from(futureTask, 1000, TimeUnit.MILLISECONDS);
        System.out.println(System.currentTimeMillis() + ": subscribe");
        fromWithTimeout.subscribe(
                i -> System.out.println(System.currentTimeMillis() + ": onNext::" + i),
                e -> System.out.println(System.currentTimeMillis() + ": onError:: " + e), // if timeout < future time
                () -> System.out.println(System.currentTimeMillis() + ": onComplete")
        );

        System.out.println('\n' + "::from array::");
        Integer[] array = {1, 2, 4};
        Observable<Integer> fromArrayObservable = Observable.from(array);
        fromArrayObservable.subscribe(
                i -> System.out.println("onNext::" + i),
                e -> System.out.println("onError:: " + e),
                () -> System.out.println("onComplete")
        );
    }
}

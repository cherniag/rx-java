package gc.learn.rxjava.core;

import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

import java.util.concurrent.TimeUnit;

// Subject extends Observable and implements Observer
public class SubjectL {
    public static void main(String[] args) throws InterruptedException {
        publishSubject();
        replaySubject();
        behaviorSubject();
        asyncSubject();
    }

    private static void publishSubject() {
        System.out.println('\n' + "::publishSubject::");
        PublishSubject<Integer> publishSubject = PublishSubject.create();
        publishSubject.onNext(1);    // will not be printed - not subscribed yet
        publishSubject.subscribe(System.out::println,    // onNext
                e -> System.out.println("error:: " + e), // onError
                () -> System.out.println("complete")     // onComplete
        );
        publishSubject.onNext(2);
        publishSubject.onNext(3);
        publishSubject.onCompleted(); // complete
        publishSubject.onNext(4);     // will not be printed
    }

    //PublishSubject implements Observable and Observer
    private static void replaySubject() throws InterruptedException {
        System.out.println('\n' + "::replaySubject::");
        ReplaySubject<Integer> replaySubject = ReplaySubject.create();
        replaySubject.onNext(1);    // WILL BE PRINTED - not subscribed yet but cached
       /* replaySubject.subscribe(System.out::println,    // onNext
                e -> System.out.println("error:: " + e), // onError
                () -> System.out.println("complete")     // onComplete
        );  */
        replaySubject.subscribe(System.out::println);    // just only onNext
        replaySubject.onNext(2);
        replaySubject.onNext(3);
        replaySubject.onCompleted(); // complete
        replaySubject.onNext(4);     // will not be printed

        System.out.println('\n' + "::replaySubjectWithSize::");
        ReplaySubject<Integer> replaySubjectWithSize = ReplaySubject.createWithSize(2);
        replaySubjectWithSize.onNext(1);    // WILL NOT BE PRINTED - not fit in size 2
        replaySubjectWithSize.onNext(2);    // WILL BE PRINTED - fits in size 2
        replaySubjectWithSize.onNext(3);    // WILL BE PRINTED - fits in size 2
        replaySubjectWithSize.subscribe(System.out::println);    // just only onNext
        replaySubjectWithSize.onCompleted(); // complete
        replaySubjectWithSize.onNext(4);     // will not be printed

        System.out.println('\n' + "::replaySubjectWithTime::");
        ReplaySubject<Integer> replaySubjectWithTime = ReplaySubject
                .createWithTime(500, TimeUnit.MILLISECONDS, Schedulers.immediate());
        replaySubjectWithTime.onNext(1);    // WILL NOT BE PRINTED - will expire - 200 + 400 ms
        Thread.sleep(200);
        replaySubjectWithTime.onNext(2);    // WILL BE PRINTED - will not expire - 400 ms
        replaySubjectWithTime.onNext(3);    // WILL BE PRINTED - will not expire - 400 ms
        Thread.sleep(400);
        replaySubjectWithTime.subscribe(System.out::println);    // just only onNext
        replaySubjectWithTime.onCompleted(); // complete
        replaySubjectWithTime.onNext(4);     // will not be printed

        //ReplaySubject.createWithTimeAndSize - both
    }

    private static void behaviorSubject() {
        System.out.println('\n' + "::behaviorSubject::");
        BehaviorSubject<Integer> behaviorSubject = BehaviorSubject.create();
        behaviorSubject.onNext(1);
        behaviorSubject.onNext(2);
        behaviorSubject.onNext(3); // only last value will be stored = ReplaySubject with size 1
        behaviorSubject.subscribe(System.out::println);
        behaviorSubject.onCompleted();
        behaviorSubject.onNext(4);

        System.out.println('\n' + "::behaviorSubjectWithInit::");
        BehaviorSubject<Integer> behaviorSubjectWithInit = BehaviorSubject.create(5);
        behaviorSubjectWithInit.subscribe(System.out::println); // will be printed init value before any onNext calls
        behaviorSubjectWithInit.onNext(1);
        behaviorSubjectWithInit.onNext(2);
        behaviorSubjectWithInit.onCompleted();
        behaviorSubjectWithInit.onNext(4);
    }

    private static void asyncSubject() {
        System.out.println('\n' + "::asyncSubject::");
        AsyncSubject<Integer> asyncSubject = AsyncSubject.create();
        asyncSubject.onNext(1);
        asyncSubject.onNext(2);
        asyncSubject.onNext(3);
        asyncSubject.subscribe(System.out::println);
        System.out.println("subscribed but no print until onCompleted");
        asyncSubject.onCompleted(); // prints only the last value
        asyncSubject.onNext(4);
    }
}

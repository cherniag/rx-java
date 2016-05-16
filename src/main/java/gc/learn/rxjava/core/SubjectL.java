package gc.learn.rxjava.core;

import rx.Observable;
import rx.subjects.PublishSubject;


public class SubjectL {
    public static void main(String[] args) {
        publishSubject();
    }

    //PublishSubject implements Observable and Observer
    private static void publishSubject() {
        PublishSubject<Integer> publishSubject = PublishSubject.create();
        publishSubject.onNext(1);    // will not be printed - not subscribed yet
        publishSubject.subscribe(System.out::println,    // onNext
                e -> System.out.println("error:: " + e), // onError
                () -> System.out.println("complete")     // onComplete
        );   // onComplete
        publishSubject.onNext(2);
        publishSubject.onNext(3);
        publishSubject.onCompleted(); // complete
        publishSubject.onNext(4);     // will not be printed
    }
}

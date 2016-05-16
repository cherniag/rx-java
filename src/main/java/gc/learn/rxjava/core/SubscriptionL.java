package gc.learn.rxjava.core;

import rx.Subscription;
import rx.subjects.PublishSubject;


public class SubscriptionL {
    public static void main(String[] args) {
        unsubscribe();
    }

    private static void unsubscribe() {
        System.out.println('\n' + "::unsubscribe::");
        PublishSubject<Integer> publishSubject = PublishSubject.create();
        Subscription subscription1 = publishSubject.subscribe(i -> System.out.println("subscription1: " + i));
        Subscription subscription2 = publishSubject.subscribe(i -> System.out.println("subscription2: " + i));
        publishSubject.onNext(1);
        publishSubject.onNext(2);
        subscription1.unsubscribe(); // unsubscribes only 1st subscription1
        publishSubject.onNext(3);    // print to subscription2 only
    }
}

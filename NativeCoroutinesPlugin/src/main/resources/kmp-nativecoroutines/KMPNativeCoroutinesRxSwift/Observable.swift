//
//  Observable.swift
//  KMPNativeCoroutinesRxSwift
//
//  Created by Rick Clephas on 05/07/2021.
//

import RxSwift

/// Creates an `Observable` for the provided `NativeFlow`.
/// - Parameter nativeFlow: The native flow to collect.
/// - Returns: An observable that publishes the collected values.
public func createObservable<Output, Failure: Error, Unit>(
    for nativeFlow: @escaping NativeFlow<Output, Failure, Unit>,
    isMainThread: Bool = true
) -> Observable<Output> {
    return createObservableImpl(for: nativeFlow, isMainThread: isMainThread)
}

/// Creates an `Observable` for the provided `NativeFlow`.
/// - Parameter nativeFlow: The native flow to collect.
/// - Returns: An observable that publishes the collected values.
public func createObservable<Unit, Failure: Error>(
    for nativeFlow: @escaping NativeFlow<Unit, Failure, Unit>,
    isMainThread: Bool = true
) -> Observable<Void> {
    return createObservableImpl(for: nativeFlow, isMainThread: isMainThread).map { _ in }
}

private func createObservableImpl<Output, Failure: Error, Unit>(
    for nativeFlow: @escaping NativeFlow<Output, Failure, Unit>,
    isMainThread: Bool = true
) -> Observable<Output> {
    
    return Observable.deferred {
        return Observable.create { observer in
            let nativeCancellable = nativeFlow({ item, next, _ in
                if isMainThread {
                    DispatchQueue.main.async {
                        observer.onNext(item)
                    }
                } else {
                    observer.onNext(item)
                }
                return next()
            }, { error, unit in
                if isMainThread {
                    DispatchQueue.main.async {
                        if let error = error {
                            observer.onError(error)
                        } else {
                            observer.onCompleted()
                        }
                    }
                } else {
                    if let error = error {
                        observer.onError(error)
                    } else {
                        observer.onCompleted()
                    }
                }

                return unit
            }, { cancellationError, unit in
                if isMainThread {
                    DispatchQueue.main.async {
                        observer.onError(cancellationError)
                    }
                } else {
                    observer.onError(cancellationError)
                }
                return unit
            })
            return Disposables.create { _ = nativeCancellable() }
        }
    }
}

import Foundation

// sourcery: AutoMockable
protocol CacheDataSource: Sendable {
    var signedInUserStream: AsyncStream<Bool> { get async }
    func updateSignedInUser(_ value: Bool) async
}

actor CacheDataSourceImpl: CacheDataSource {
    private var signedInUser: Bool = false
    private var continuation: AsyncStream<Bool>.Continuation?

    var signedInUserStream: AsyncStream<Bool> {
        get async {
            AsyncStream { continuation in
                self.continuation = continuation
                continuation.yield(signedInUser)
            }
        }
    }

    func updateSignedInUser(_ value: Bool) async {
        signedInUser = value
        continuation?.yield(value)
    }
}

// swift-tools-version: 6.0
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "PracticAppModules",
    defaultLocalization: "en",
    platforms: [.iOS(.v17)],
    products: [
        // Products define the executables and libraries a package produces, and make them visible to other packages.
        .library(
            name: "PracticAppModules",
            targets: [
                "AppCommon",
                "AppNavigation",
                "Logger"
            ]
        )
    ],
    dependencies: [
        .package(url: "https://github.com/hmlongco/Factory", from: "2.4.3"),
        .package(url: "https://github.com/firebase/firebase-ios-sdk", from: "11.6.0"),
        .package(url: "https://github.com/siteline/swiftui-introspect", from: "1.3.0"),
        .package(url: "https://github.com/dfed/swift-testing-expectation", from: "0.1.3"),
        .package(url: "https://github.com/onevcat/Kingfisher.git", from: "8.3.2")
    ],
    targets: [
        .target(
            name: "AppCommon",
            dependencies: [
                "Data",
                "Domain",
                "FoundationUtils",
                "AppModels",
                .product(name: "Factory", package: "Factory"),
                .product(name: "SwiftUIIntrospect", package: "swiftui-introspect"),
                "Kingfisher"
            ]
        ),
        .target(
            name: "AppNavigation",
            dependencies: [
                "AppCommon",
                "AppModels",
                .product(name: "FirebaseCrashlytics", package: "firebase-ios-sdk"),
                "Logger",
                "FeatureLogin",
                "FeatureStudent",
                "FeatureWorkTutor",
                "FeatureCompany"
            ]
        ),
        .target(
            name: "AppModels",
            dependencies: []
        ),
        .target(
            name: "Data", dependencies: [
                "Domain",
                "FoundationUtils",
                "Logger",
                .product(name: "Factory", package: "Factory"),
                .product(name: "FirebaseRemoteConfig", package: "firebase-ios-sdk"),
                .product(name: "FirebaseAuth", package: "firebase-ios-sdk")
            ]
        ),
        .target(
            name: "Domain",
            dependencies: ["FoundationUtils"]
        ),
        .target(
            name: "FeatureLogin",
            dependencies: [
                "AppCommon",
                .product(name: "Factory", package: "Factory")
            ]
        ),
        .target(
            name: "FeatureStudent",
            dependencies: [
                "AppCommon",
                .product(name: "Factory", package: "Factory")
            ]
        ),
        .target(
            name: "FeatureCompany",
            dependencies: [
                "AppCommon",
                .product(name: "Factory", package: "Factory")
            ]
        ),
        .target(
            name: "FeatureWorkTutor",
            dependencies: [
                "AppCommon",
                "FeatureCompany",
                "AppModels",
                .product(name: "Factory", package: "Factory")
            ]
        ),
        .target(
            name: "FoundationUtils"
        ),
        .target(
            name: "Logger",
            dependencies: [
                .product(name: "Factory", package: "Factory"),
                .product(name: "FirebaseCrashlytics", package: "firebase-ios-sdk")
            ]
        ),
        .target(
            name: "Mocks",
            dependencies: [
                "Domain",
                "Data"
            ]
        ),
        // Test Targets
        .testTarget(
            name: "AppCommonTests", dependencies: ["AppCommon"]
        ),
        .testTarget(
            name: "DataTests",
            dependencies: [
                "Data",
                "Mocks"
            ],
            resources: [.process("res")]
        ),
        .testTarget(
            name: "DomainTests",
            dependencies: [
                "Domain",
                "Mocks"
            ]
        ),
        .testTarget(
            name: "FeatureLoginTests",
            dependencies: [
                "FeatureLogin",
                "Mocks",
                .product(name: "Factory", package: "Factory"),
                .product(name: "TestingExpectation", package: "swift-testing-expectation")
            ]
        ),
        .testTarget(
            name: "FoundationUtilsTests",
            dependencies: ["FoundationUtils"]
        )
    ]
)

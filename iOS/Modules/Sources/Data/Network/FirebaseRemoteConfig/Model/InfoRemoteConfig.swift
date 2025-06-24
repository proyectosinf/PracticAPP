import Foundation

/**
  Represents the remote config from Firebase

  ## Firebase Config
 Should be added to Firebase Remote config with the keys:
 - `features_ios` for production
 - `features_ios_pre` for development

  ```json
 "update": {
     "isActive": false,
     "fromVersion": "0.0.0",
     "toVersion": "0.0.0",
     "force": true,
     "urlStore": "https://apps.apple.com/es/app/apple-configurator/id1588794674"
 },
 "features": [
     {
         "name": "feature_x",
         "isActive": false,
         "fromVersion": "0.0.0",
         "toVersion": "0.0.0"
     }
 ]
  ```
  */
struct InfoRemoteConfig: Codable, Hashable, Sendable {
    let update: AppUpdateRemoteConfig?
    let features: [FeatureFlagRemoteConfig]?
}

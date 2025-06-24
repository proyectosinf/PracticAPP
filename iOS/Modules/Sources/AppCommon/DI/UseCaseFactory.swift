import Data
import Domain
import Factory
import Foundation

public extension Container {
    var updateAppUseCase: Factory<UpdateAppUseCase> {
        self { UpdateAppUseCaseImpl(appSettingsRepository: self.appSettingsRepository()) }
    }

    var featureEnabledUseCase: Factory<FeatureEnabledUseCase> {
        self { FeatureEnabledUseCaseImpl(appSettingsRepository: self.appSettingsRepository()) }
    }
}

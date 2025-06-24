fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

## iOS

### ios test

```sh
[bundle exec] fastlane ios test
```

Runs App and Modules tests

### ios release

```sh
[bundle exec] fastlane ios release
```

Release version

### ios qa_distribution

```sh
[bundle exec] fastlane ios qa_distribution
```

Publish beta in Firebase

### ios branch_qa_distribution

```sh
[bundle exec] fastlane ios branch_qa_distribution
```

Publish beta in Firebase

### ios release_candidate

```sh
[bundle exec] fastlane ios release_candidate
```

Publish release candidate in Firebase

### ios add_device

```sh
[bundle exec] fastlane ios add_device
```

Register device

### ios install_certs

```sh
[bundle exec] fastlane ios install_certs
```

Install certificates in your computer, this not generate new certs, just download them from the repository.

to regenerate them use the lane update_certs

By default it install the release certs, for adhoc or development add the param --env [adhoc, development]

### ios update_certs

```sh
[bundle exec] fastlane ios update_certs
```

Update certificates

### ios reset_certs

```sh
[bundle exec] fastlane ios reset_certs
```

Reset certificates

### ios localize

```sh
[bundle exec] fastlane ios localize
```

Update localizables

### ios firebase_udids

```sh
[bundle exec] fastlane ios firebase_udids
```

Download Firebase UDIDs

### ios test_lanes

```sh
[bundle exec] fastlane ios test_lanes
```



----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).

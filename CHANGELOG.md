# Change Log

All notable changes to this project will be documented in this file. 

The format is based on [Keep a Changelog](http://keepachangelog.com)

## [Unreleased]

### Added
- bnd-baseline-maven-plugin has been added to core and webster modules to enforce semantic versioning of the API.
- Added runtime options to cli (--store-blobs) and maven plugin (-DstoreBlobs) to trade higher memory usage for lower I/O overhead.

### Changed
- CLI now redirects System.out to System.err before loading progress checks.
- CLI and maven plugin no longer cache blobs by default.
- Module README.md files now used as source for about / index.html for maven-site-plugin.

### Fixed
- GH16 relocated default target files under single oakpal-plugin folder

## [1.4.0] - 2019-09-03

### Added
- Added oakpal-cli and Dockerfile

### Changed
- Introduced blob caching for cli and maven execution by default

### Fixed
- 100% test coverage uncovered several bugs which have been fixed

### Removed
- Removed aem and interactive modules
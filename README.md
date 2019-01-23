#Passport Reader Cordova Plugin

The plugin allows your Android app to read data from a biometric passport.

## Installation

1. Clone the plugin source from Github:

	~~~bash
	git clone --recurse-submodules https://github.com/AppliedRecognition/Passport-Reader-Cordova.git
	~~~
1. Install the plugin in your Cordova project replacing `path/to/plugin` with the location of the plugin you cloned in the previous step:

	~~~bash
	cordova plugin add path/to/plugin
	~~~
1. Build your project:

	~~~bash
	cordova build android
	~~~

## Scanning Passports

~~~javascript
// This object holds the information needed to start the scan
var bacSpec = {
	// Passport number
	"documentNumber": "123456789",
	// Date of birth of the passport holder in YYYY-MM-DD format
	"dateOfBirth": "1970-01-01",
	// Passport's date of expiry in YYYY-MM-DD format
	"dateOfExpiry": "2030-01-01",
};
// Start the scan
passportreader.scanPassport(bacSpec, function(result) {
	if (result == null) {
		// The user canceled the scan
		return;
	}
	// See module documentation for result properties:
	// https://appliedrecognition.github.io/Passport-Reader-Cordova/module-passportreader.html
}, function(error) {
	
});
~~~

## Detecting a Face in the Result's Image

You can use the scan result's image for face detection using the [Ver-ID plugin](https://github.com/AppliedRecognition/Ver-ID-Person-Cordova-Plugin).

After installing the Ver-ID plugin following the instructions above Ver-ID will be available in `window.verid`.

See the [`Face`](https://appliedrecognition.github.io/Ver-ID-Person-Cordova-Plugin/module-verid.html#~Face) type documentation for the properties of the returned face. You can pass the face's `faceTemplate` to Ver-ID's [`compareFaceTemplates `](https://appliedrecognition.github.io/Ver-ID-Person-Cordova-Plugin/module-verid.html#.compareFaceTemplates) function.

~~~javascript
// Scan the passport
passportreader.scanPassport(bacSpec, function(result) {
	if (result == null) {
		// The user canceled the scan
		return;
	}
	if (result.image) {	
		verid.detectFaceInImage(result.image, function(face) {
		    // Face detected
		}, function(error) {
		    // Face detection failed
		});
	}
}, function(error) {
	
});
~~~


## API Reference

[Module documentation](https://appliedrecognition.github.io/Passport-Reader-Cordova/module-passportreader.html)
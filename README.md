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
	// 
}, function(error) {
	
});
~~~
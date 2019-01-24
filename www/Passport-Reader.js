var exec = require("cordova/exec");

var PLUGIN_NAME = "PassportReaderPlugin";

/** @module passportreader */

/** 
 * Basic Access Control (BAC) specification
 * @typedef BACSpec
 * @type {object}
 * @property {string} documentNumber Passport number
 * @property {string} dateOfBirth Date of birth of the passport holder in YYYY-MM-DD format
 * @property {string} dateOfExpiry Passport's date of expiry in YYYY-MM-DD format
 */

/**
 * Passport scan result
 * @typedef ScanResult
 * @type {object}
 * @property {string} documentCode Document code
 * @property {string} issuingState Issuing state
 * @property {string} primaryIdentifier Primary identifier (e.g., last name)
 * @property {Array<string>} secondaryIdentifiers Secondary identifiers (e.g., given names)
 * @property {string} nationality Nationality of the passport holder
 * @property {string} documentNumber Document number
 * @property {string} personalNumber Personal number
 * @property {string} dateOfBirth Date of birth of the passport holder YYYY-MM-DD
 * @property {string} gender Gender of the passport holder
 * @property {string} dateOfExpiry Date of expiry of the passport YYYY-MM-DD
 * @property {string} image Image of the holder's face as URL data scheme (e.g., data:image/jpeg;base64,imageData)
 */

/** 
 * Called when passport scan finishes
 * @callback PassportScanCallback
 * @param {module:passportreader~ScanResult} result Result may be undefined or null if the user cancels the scan
 */

/**
 * Scan a biometric passport and return the data extracted from its NFC chip
 * @param {module:passportreader~BACSpec} bacSpec BAC spec
 * @param {module:passportreader~PassportScanCallback} [callback] Called when passport scan finishes
 * @param {function} [errorCallback] Called when passport scan fails
 * @returns {Promise.<module:passportreader~ScanResult>} If callback is not specified returns a promise
 */
module.exports.scanPassport = function(bacSpec, callback, errorCallback) {
    var args = [{"bacSpec":JSON.stringify(bacSpec)}];
    function parseResult(cb) {
        return function(json) {
            if (json) {
                var result = JSON.parse(json);
                cb(result);
            } else {
                cb();
            }
        }
    }
    if (callback) {
        exec(parseResult(callback), errorCallback, PLUGIN_NAME, "scanPassport", args);
    } else {
        return new Promise(function(resolve, reject) {
            exec(parseResult(resolve), reject, PLUGIN_NAME, "scanPassport", args);
        });
    }
}
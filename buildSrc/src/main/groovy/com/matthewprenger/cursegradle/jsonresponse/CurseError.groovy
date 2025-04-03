package com.matthewprenger.cursegradle.jsonresponse

/**
 * Returned as JSON when an error occurs
 */
class CurseError {

    /**
     * The HTTP status code
     */
    int errorCode

    /**
     * The error message
     */
    String errorMessage

    @Override
    String toString() {
        return "CurseError{" +
                "errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                '}'
    }
}

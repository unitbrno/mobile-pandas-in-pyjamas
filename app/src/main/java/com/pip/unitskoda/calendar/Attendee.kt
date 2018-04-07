package com.pip.unitskoda.calendar

import java.io.Serializable

data class Attendee(val name: String, val email: String, val status: String, val models: List<String>): Serializable{
}